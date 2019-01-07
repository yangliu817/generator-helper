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

        if (StringUtils.isEmpty(packageName)) {
            code = code.replace("[package]", "");
        } else {
            code = code.replace("[package]", "package " + packageName + ";");
        }

        code = code.replace("[className]", className);
        List<String> imports = new ArrayList<>();
        if (source.isMybatisPlus()) {
            code = code.replace("[methods]", "");
            if (source.getUseBaseService()) {

                imports.add(ApplicationContant.config.getProperty("IService"));
                imports.add(source.getEntitySource().getClassFullName());
                String extendCode = " extends IService<" + source.getEntitySource().getShortName() + ">";
                code = code.replace("[extends]", extendCode);

            } else {
                code = code.replace("[extends]", "");
                code = code.replace("[imports]", "");
            }
        } else {
            String templateCode = template.t_abstract_methods_normal;
            if (source.isContainsPrimaryKey()) {
                templateCode = templateCode + template.t_abstract_methods_needprimarykey;
            }
            code = generateAbstractMethods(code, templateCode, source.getEntitySource(), imports);
        }

        code = generateImports(code, imports);
        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }
}
