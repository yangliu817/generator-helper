package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.*;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The type Service impl generator.
 */
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
        code = generateCopyRight(code, source);
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
            extendsCode =
                    " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">";

        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                && source.getUseBaseService() && source.getCreateInterface()) {
            imports.add(source.getServiceSource().getClassFullName());
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                && source.getUseBaseService() && !source.getCreateInterface()) {
            extendsCode =
                    " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">";
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

        //mybatis 创建baseService 并且创建接口
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && source.getUseBaseService() && source.getMapperSource().getExtendBaseMapper()) {
            String mybatisServiceImplPackage = generateMybatisBaseService(source);
            String baseServiceImplCode = template.t_service_impl_base_mybatis;
            baseServiceImplCode = generateCopyRight(baseServiceImplCode, source);
            baseServiceImplCode = generateComments(baseServiceImplCode, source);
            String filename = "MybatisServiceImpl.java";
            String basePackage = source.getProjectSetting().getProjectPackage();
            String codePath = source.getProjectSetting().getCodePath();
            if (!codePath.endsWith("/")) {
                codePath = codePath + "/";
            }
            String mybatisServiceImplPath = codePath + "src/main/java/" + basePackage.replace(".", "/") + "/base/";

            baseServiceImplCode = baseServiceImplCode.replace("[package]", "package " + mybatisServiceImplPackage +
                    ";");

            File file = new File(mybatisServiceImplPath, filename);
            if (!file.exists()) {
                FileUtils.output(baseServiceImplCode, mybatisServiceImplPath, filename);
            }
            imports.add(source.getEntitySource().getClassFullName());
            imports.add(mybatisServiceImplPackage + ".MybatisService");
            imports.add(mybatisServiceImplPackage + ".MybatisServiceImpl");
            String primaryKeyType = getClassShortName(source.getEntitySource().getPrimaryKeyType());
            extendsCode =
                    " extends MybatisServiceImpl<" + source.getEntitySource().getEntityName() + ", " + primaryKeyType + ", " + source.getMapperSource().getShortName() + ">";

            code = code.replace("[methods]", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && !source.getUseBaseService()) {
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
            imports.add(ApplicationContant.config.getProperty("Optional"));
            code = generateComponentFields(code, fieldType);
            methodCode = methodCode.replace("[entityClass-l]",
                    CodeUtils.firstChar2Lowercase(source.getEntitySource().getEntityName()));
            methodCode = generateConditions(source.getEntitySource(), imports, methodCode);
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && !source.getCreateInterface()) {
            methodCode = methodCode.replace("    @Override\n", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && source.getCreateInterface()) {
            imports.add(source.getServiceSource().getClassFullName());
            implementsCode = " implements " + source.getServiceSource().getShortName();
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && source.getCreateInterface() && source.getUseBaseService()) {
            imports.add(source.getRepositorySource().getClassFullName());
            imports.add(source.getEntitySource().getClassFullName());
            String templateBaseServiceImplCode = template.t_service_impl_base_jpa;
            String importCode = "import " + source.getServiceSource().getFullPackage() + ".JpaService;\n"
                    + "import " + source.getRepositorySource().getFullPackage() + ".BaseRepository;\n";
            templateBaseServiceImplCode = templateBaseServiceImplCode.replace("[baseServiceImport]", importCode);
            generateJpaService(source, templateBaseServiceImplCode, source.getFilepath(), "JpaServiceImpl.java");
            imports.add(source.getServiceSource().getClassFullName());
            methodCode = "";
            implementsCode = " implements " + source.getServiceSource().getShortName();
            extendsCode =
                    " extends JpaServiceImpl<" + source.getEntitySource().getEntityName() + ", " + getClassShortName(source.getEntitySource().getPrimaryKeyType()) + ", " + source.getRepositorySource().getShortName() + ">";
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && !source.getCreateInterface() && source.getUseBaseService()) {
            imports.add(source.getRepositorySource().getClassFullName());
            imports.add(source.getEntitySource().getClassFullName());
            String templateBaseServiceImplCode = template.t_service_impl_base_jpa;
            templateBaseServiceImplCode = templateBaseServiceImplCode.replace("[baseServiceImport]\n", "");
            generateJpaService(source, templateBaseServiceImplCode, source.getFilepath(), "JpaServiceImpl.java");
            generateJpaService(source, template.t_service_base_jpa, source.getFilepath(), "JpaService.java");
            methodCode = "";
            implementsCode =
                    " implements JpaService<" + source.getEntitySource().getEntityName() + ", " + getClassShortName(source.getEntitySource().getPrimaryKeyType()) + ">";
            extendsCode =
                    " extends JpaServiceImpl<" + source.getEntitySource().getEntityName() + ", " + getClassShortName(source.getEntitySource().getPrimaryKeyType()) + ", " + source.getRepositorySource().getShortName() + ">";
        }


        code = code.replace("[methods]", methodCode);
        code = code.replace("[mapperName]", CodeUtils.firstChar2Lowercase(source.getMapperSource().getShortName()));
        code = code.replace("[repositoryName]",
                CodeUtils.firstChar2Lowercase(source.getRepositorySource().getShortName()));
        code = code.replace("[entityClass]", source.getEntitySource().getEntityName());
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

    /**
     * Generate jpa service.
     *
     * @param source       the source
     * @param templateCode the template code
     * @param path         the path
     * @param filename     the filename
     */
    protected void generateJpaService(Source source, String templateCode, String path, String filename) {
        ServiceImplSource serviceSource = (ServiceImplSource) source;
        templateCode = generateComments(templateCode, serviceSource);
        templateCode = generateCopyRight(templateCode, serviceSource);
        templateCode = templateCode.replace("[package]", "package " + serviceSource.getFullPackage() + ";");
        File file = new File(serviceSource.getFilepath(), filename);
        if (!file.exists()) {
            FileUtils.output(templateCode, path, filename);
        }
    }

    /**
     * Generate conditions string.
     *
     * @param entitySource the entity source
     * @param imports      the imports
     * @param code         the code
     * @return the string
     */
    protected String generateConditions(EntitySource entitySource, List<String> imports, String code) {
        List<DBUtils.ColumInfo> columInfos = entitySource.getTableInfo().getColumInfos();
        imports.add(ApplicationContant.config.getProperty("Path"));
        imports.add(ApplicationContant.config.getProperty("Optional"));
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
            if (Objects.equals(fieldType, "Boolean") && fieldName.startsWith("is")) {
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
