package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ServiceGenerator extends AbstractGenerator<ServiceSource> {
    @Override
    public void generate(ServiceSource source) {
        String code = template.t_service;
        code = generateComments(code, source);
        code = generateCopyRight(code, source);
        String className = source.getShortName();

        code = generatePackage(source, code);

        code = code.replace("[className]", className);

        List<String> imports = new ArrayList<>();

        String extendCode = "";

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && source.getUseBaseService()) {
            imports.add(ApplicationContant.config.getProperty("IService"));
            imports.add(source.getEntitySource().getClassFullName());
            extendCode = " extends IService<" + source.getEntitySource().getEntityName() + ">";
            code = code.replace("[methods]", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) && source.getUseBaseService()) {
            String baseServiceCode = template.t_service_base_jpa;
            code = generateCopyRight(code, source);
            baseServiceCode = generateCopyRight(baseServiceCode, source);
            baseServiceCode = generateComments(baseServiceCode, source);
            baseServiceCode = baseServiceCode.replace("[package]", "package " + source.getFullPackage() + ";");
            String filename = "JpaService.java";
            File file = new File(source.getFilepath(), filename);
            if (!file.exists()) {
                FileUtils.output(baseServiceCode, source.getFilepath(), filename);
            }
            imports.add(source.getEntitySource().getClassFullName());
            String primaryKeyType = getClassShortName(source.getEntitySource().getPrimaryKeyType());
            extendCode = " extends JpaService<" + source.getEntitySource().getShortName() + ", " + primaryKeyType + ">";
            code = code.replace("[methods]", "");
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && source.getUseBaseService() && source.getMapperSource().getExtendBaseMapper()) {
            String mybatisServicePackage = generateMybatisBaseService(source);
            imports.add(source.getEntitySource().getClassFullName());
            imports.add(mybatisServicePackage + ".MybatisService");
            String primaryKeyType = getClassShortName(source.getEntitySource().getPrimaryKeyType());
            extendCode = " extends MybatisService<" + source.getEntitySource().getShortName() + ", " + primaryKeyType + ">";
            code = code.replace("[methods]", "");
        }

        code = code.replace("[extends]", extendCode);

        boolean flag = !((Objects.equals(source.getOrmType(), OrmTypeEnum.JPA) || Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis)) && source.getUseBaseService());

        if (flag) {
            code = generateAbstractMethods(code, source.getEntitySource(), imports);
        }

        code = generateImports(code, imports);
        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
