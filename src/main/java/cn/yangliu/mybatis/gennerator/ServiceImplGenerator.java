package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.EntitySource;
import cn.yangliu.mybatis.source.ServiceImplSource;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class ServiceImplGenerator extends AbstractGenerator<ServiceImplSource> {

    @Autowired
    private ServiceGenerator serviceGenerator;

    @Override
    public void generate(ServiceImplSource source) {
        if (source.getCreateInterface()) {
            ServiceSource serviceSource = source.getServiceSource();
            serviceGenerator.generate(serviceSource);
        }

        String code = template.t_service_impl;
        code = generateComments(code, source);
        String className = source.getShortName();
        code = code.replace("[className]", className);

        code = generatePackage(source, code);

        List<String> imports = new ArrayList<>();
        List<String> anontations = new ArrayList<>();

        imports.add(ApplicationContant.config.getProperty("Service"));
        anontations.add("Service");

        String methodCode = "";
        String fieldCode = "";
        String implementsCode = "";
        String extendsCode = "";
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && source.getUseBaseService()) {
            imports.add(source.getEntitySource().getClassFullName());
            imports.add(source.getMapperSource().getClassFullName());
            imports.add(ApplicationContant.config.getProperty("ServiceImpl"));
            implementsCode = " implements " + source.getServiceSource().getShortName();
            extendsCode = " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">";

        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                && source.getUseBaseService() && source.getCreateInterface()) {
            imports.add(source.getServiceSource().getClassFullName());
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                && source.getUseBaseService() && !source.getCreateInterface()) {
            extendsCode = " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">";
            imports.add(ApplicationContant.config.getProperty("IService"));
            imports.add(source.getEntitySource().getClassFullName());
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && !source.getUseBaseService()) {
            String fieldType = source.getMapperSource().getShortName();
            imports.add(source.getMapperSource().getClassFullName());
            code = generateComponentFields(code, fieldType);
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                && !source.getUseBaseService() && source.getCreateInterface()) {
            implementsCode = " implements " + source.getServiceSource().getShortName();
            imports.add(source.getServiceSource().getClassFullName());
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis)) {
            imports.add(ApplicationContant.config.getProperty("Autowired"));
            imports.add(ApplicationContant.config.getProperty("List"));
            imports.add(source.getEntitySource().getClassFullName());

            String fieldType = source.getMapperSource().getShortName();
            imports.add(source.getMapperSource().getClassFullName());
            code = generateComponentFields(code, fieldType);

            methodCode = template.t_service_impl_methods_normal;
        }
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && source.isContainsPrimaryKey()) {
            methodCode = methodCode + "\n" + template.t_service_impl_methods_needprimarykey;
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && !source.getCreateInterface()) {
            methodCode = methodCode.replace("    @Override\n", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && source.getCreateInterface()) {
            imports.add(source.getServiceSource().getClassFullName());
            implementsCode = " implements " + source.getServiceSource().getShortName();
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            methodCode = template.t_service_impl_methods_jpa;

            String fieldType = source.getRepositorySource().getShortName();
            imports.add(source.getRepositorySource().getClassFullName());
            imports.add(source.getEntitySource().getClassFullName());
            imports.add(ApplicationContant.config.getProperty("List"));
            imports.add(ApplicationContant.config.getProperty("ArrayList"));
            imports.add(ApplicationContant.config.getProperty("Specification"));
            imports.add(ApplicationContant.config.getProperty("PageJpa"));
            imports.add(ApplicationContant.config.getProperty("Pageable"));
            imports.add(ApplicationContant.config.getProperty("Predicate"));
            imports.add(ApplicationContant.config.getProperty("Autowired"));
            code = generateComponentFields(code, fieldType);
            methodCode = generateConditions(source.getEntitySource(), imports, methodCode);
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && !source.getCreateInterface()) {
            methodCode = methodCode.replace("    @Override\n", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && source.getCreateInterface()) {
            imports.add(source.getServiceSource().getClassFullName());
            implementsCode = " implements " + source.getServiceSource().getShortName();
        }

        code = code.replace("[methods]", methodCode);
        code = code.replace("[mapperName]", CodeUtils.firstChar2Lowercase(source.getMapperSource().getShortName()));
        code = code.replace("[repositoryName]", CodeUtils.firstChar2Lowercase(source.getRepositorySource().getShortName()));
        code = code.replace("[entityClass]", source.getEntitySource().getShortName());
        code = code.replace("[primaryKeyType]", getClassShortName(source.getEntitySource().getPrimaryKeyType()));

        code = code.replace("[methods]", methodCode);
        code = code.replace("[fields]", fieldCode);
        code = code.replace("[extends]", extendsCode);
        code = code.replace("[implements]", implementsCode);

        if (source.getUseTransactional()) {
            imports.add(ApplicationContant.config.getProperty("Transactional"));
            anontations.add("Transactional(rollbackFor = Throwable.class)");
        }

        code = checkUseLombok(source, code, imports, anontations);

        code = generateImports(code, imports);
        code = generateAnnotations(code, anontations);
        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }

    protected String generateConditions(EntitySource entitySource, List<String> imports, String code) {
        List<DBUtils.ColumInfo> columInfos = entitySource.getTableInfo().getColumInfos();
        Map<String, JavaType> columnMapping = entitySource.getColumnMapping();
        List<String> excludeColumns = entitySource.getExcludeColumns();
        StringBuilder sb = new StringBuilder();
        for (DBUtils.ColumInfo columInfo : columInfos) {
            String columnName = columInfo.getName();
            if (excludeColumns.contains(columnName)) {
                continue;
            }
            JavaType javaType = columnMapping.getOrDefault(columnName, JavaType.DEFAULT);
            if (javaType.isNeedImport()) {
                imports.add(javaType.getFullName());
            }
            String fieldType = javaType.getShortName();
            String fieldName = CodeUtils.getFieldName(columnName);
            String getMethod = "get" + CodeUtils.firstChar2Uppercase(fieldName);
            if (Objects.equals(fieldType, "Boolean")) {
                getMethod = "is" + CodeUtils.firstChar2Uppercase(fieldName);
            }
            String ifCode = template.t_if_jpa.replace("[getMethod]", getMethod)
                    .replace("[fieldType]", fieldType)
                    .replace("[fieldName]", fieldName);
            sb.append(ifCode);
        }

        code = code.replace("[if]", sb.toString());
        return code;
    }
}
