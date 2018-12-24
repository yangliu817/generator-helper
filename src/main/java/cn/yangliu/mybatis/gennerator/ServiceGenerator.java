package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceGenerator extends AbstractGenerator<ServiceSource> {
    @Override
    public void generate(ServiceSource source) {
        String packageName = source.getFullPackage();
        String className = source.getShortName();

        String code = template.t_service;

        if (StringUtils.isEmpty(packageName)){
            code = code.replace("[package]","");
        }else {
            code = code.replace("[package]","package "+packageName+";");
        }

        code = code.replace("[className]",className);

        if (source.getUseBaseService()){
            List<String> imports = new ArrayList<>();
            imports.add(ApplicationContant.config.getProperty("IService"));
            imports.add(source.getEntitySource().getClassFullName());
            String extendCode = " extends IService<"+source.getEntitySource().getShortName()+">";
            code = code.replace("[extends]",extendCode);
            code = generateImports(code,imports);
        }else {
            code = code.replace("[extends]","");
            code = code.replace("[imports]","");
        }

        FileUtils.output(code,source.getFilepath(),source.getFilename());
    }
}
