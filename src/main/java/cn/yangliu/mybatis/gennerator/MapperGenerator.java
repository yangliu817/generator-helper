package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.MapperSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MapperGenerator extends AbstractDaoGnerator<MapperSource> {

    @Override
    public void generate(MapperSource source) {
        List<String> imports = new ArrayList<>();

        List<String> annotations = new ArrayList<>();
        String code = template.t_mapper.replace("[className]", source.getShortName());
        code = generateComments(code, source);
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)) {
            code = code.replace("[methods]", "");
            String extendCode = "";
            if (source.getExtendBaseMapper()) {
                imports.add(ApplicationContant.config.getProperty("BaseMapper"));
                extendCode = " extends BaseMapper<" + source.getEntitySource().getShortName() + ">";
            }
            code = code.replace("[extends]", extendCode);
        }

        code = generateAbstractMethods(code, source.getEntitySource(), imports);

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
