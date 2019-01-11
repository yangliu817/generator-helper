package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ServiceGenerator extends AbstractGenerator<ServiceSource> {
    @Override
    public void generate(ServiceSource source) {
        String code = template.t_service;
        code = generateComments(code, source);

        String className = source.getShortName();

        code = generatePackage(source, code);

        code = code.replace("[className]", className);
        List<String> imports = new ArrayList<>();

        String extendCode = "";

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus) && source.getUseBaseService()) {
            imports.add(ApplicationContant.config.getProperty("IService"));
            imports.add(source.getEntitySource().getClassFullName());
            extendCode = " extends IService<" + source.getEntitySource().getShortName() + ">";
            code = code.replace("[methods]", "");
        }

        code = code.replace("[extends]", extendCode);

        code = generateAbstractMethods(code, source.getEntitySource(), imports);

        code = generateImports(code, imports);
        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
