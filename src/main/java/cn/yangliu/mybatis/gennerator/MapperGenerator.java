package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.MapperSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class MapperGenerator extends AbstractGenerator<MapperSource> {


    @Override
    public void generate(MapperSource source) {

        String code = template.t_mapper;

        List<String> imports = new ArrayList<>();

        List<String> annotations = new ArrayList<>();

        code = code.replace("[className]", source.getShortName());

        if (source.getExtendBaseMapper()) {
            imports.add(ApplicationContant.config.getProperty("BaseMapper"));
            code = code.replace("[extends]", " extends BaseMapper<" + source.getEntitySource().getShortName() + ">");
        } else {
            code = code.replace("[extends]", "");
        }

        if (source.getUseMapperAnonntation()) {
            imports.add(ApplicationContant.config.getProperty("Mapper"));
            annotations.add("Mapper");
        }

        if (StringUtils.isEmpty(source.getFullPackage())) {
            code = code.replace("[package]", "");
        } else {
            code = code.replace("[package]", "package " + source.getFullPackage() + ";");
        }


        if (!Objects.equals(source.getFullPackage(), source.getEntitySource().getFullPackage())) {
            if (StringUtils.isEmpty(source.getEntitySource().getFullPackage())) {
                throw new NullPointerException();
            }
            imports.add(source.getEntitySource().getClassFullName());
        }

        code = generateImports(code, imports);

        code = generateAnnontations(code, annotations);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
