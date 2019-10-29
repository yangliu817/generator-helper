package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.MapperSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Mapper generator.
 */
@Component
public class MapperGenerator extends AbstractDaoGnerator<MapperSource> {

    @Override
    public void generate(MapperSource source) {
        List<String> imports = new ArrayList<>();

        List<String> annotations = new ArrayList<>();
        String code = template.t_mapper.replace("[className]", source.getShortName());
        code = generateComments(code, source);
        code = generateCopyRight(code,source);
        String methodCode = "";
        String extendCode = "";
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && source.getExtendBaseMapper()) {
            imports.add(ApplicationContant.config.getProperty("BaseMapper"));
            extendCode = " extends BaseMapper<" + source.getEntitySource().getShortName() + ">";
        }


        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && !source.getExtendBaseMapper()) {
            code = generateAbstractMethods(code, source.getEntitySource(), imports);
        }
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && !source.getExtendBaseMapper()) {
            code = generateAbstractMethods(code, source.getEntitySource(), imports);
        }

        //mybatis 创建baseService 并且创建接口
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis) && source.getExtendBaseMapper()) {
            String basePackage = source.getProjectSetting().getProjectPackage();
            String mybatisMapperPackage = basePackage + ".base";
            String baseMybatisMapperCode = template.t_mapper_base_mybatis;
            baseMybatisMapperCode = generateCopyRight(baseMybatisMapperCode,source);
            baseMybatisMapperCode = generateComments(baseMybatisMapperCode, source);
            String filename = "MybatisMapper.java";
            String codePath = source.getProjectSetting().getCodePath();
            if (!codePath.endsWith("/")) {
                codePath = codePath + "/";
            }
            String mybatisServiceImplPath = codePath + "src/main/java/" + basePackage.replace(".", "/") + "/base/";

            baseMybatisMapperCode = baseMybatisMapperCode.replace("[package]", "package " + mybatisMapperPackage + ";");

            File file = new File(mybatisServiceImplPath, filename);
            if (!file.exists()) {
                FileUtils.output(baseMybatisMapperCode, mybatisServiceImplPath, filename);
            }
            imports.add(source.getEntitySource().getClassFullName());
            imports.add(mybatisMapperPackage + ".MybatisMapper");
            String primaryKeyType = getClassShortName(source.getEntitySource().getPrimaryKeyType());
            extendCode = " extends MybatisMapper<" + source.getEntitySource().getEntityName() + ", " + primaryKeyType + ">";

            code = code.replace("[methods]", "");
        }

        code = code.replace("[extends]", extendCode);

        code = code.replace("[methods]", methodCode);
        if (source.getUseMapperAnonntation()) {
            imports.add(ApplicationContant.config.getProperty("Mapper"));
            annotations.add("Mapper");
        }

        code = generatePackage(source, code);

        imports.add(source.getEntitySource().getClassFullName());

        code = generateImports(code, imports);

        code = generateAnnotations(code, annotations);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
