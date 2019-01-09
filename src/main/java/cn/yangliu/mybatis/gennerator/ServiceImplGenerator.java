package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.ServiceImplSource;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        code = generateComments(code,source);
        String className = source.getShortName();
        code = code.replace("[className]", className);

        if (StringUtils.isEmpty(source.getFullPackage())) {
            code = code.replace("[package]", "");
        } else {
            code = code.replace("[package]", "package " + source.getFullPackage() + ";");
        }


        List<String> imports = new ArrayList<>();
        List<String> anontations = new ArrayList<>();

        imports.add(ApplicationContant.config.getProperty("Service"));
        anontations.add("Service");
        if (source.isMybatisPlus()) {
            code = code.replace("[methods]", "");
            if (source.getUseBaseService()) {
                code = code.replace("[fields]", "");

                imports.add(source.getEntitySource().getClassFullName());
                imports.add(source.getMapperSource().getClassFullName());

                if (source.getCreateInterface()) {
                    code = code.replace("[implements]", " implements " + source.getServiceSource().getShortName());

                    imports.add(ApplicationContant.config.getProperty("ServiceImpl"));

                    imports.add(source.getServiceSource().getClassFullName());

                    code = code.replace("[extends]", " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">");
                } else {
                    String extendCode = " extends ServiceImpl<" + source.getMapperSource().getShortName() + "," + source.getEntitySource().getShortName() + ">";
                    imports.add(ApplicationContant.config.getProperty("ServiceImpl"));
                    imports.add(ApplicationContant.config.getProperty("IService"));
                    code = code.replace("[extends]", extendCode);
                    code = code.replace("[implements]", "");

                    imports.add(source.getEntitySource().getClassFullName());
                }
            } else {

                String fieldType = source.getMapperSource().getShortName();
                imports.add(source.getMapperSource().getClassFullName());
                code = generateComponentFields(code, fieldType);

                if (source.getCreateInterface()) {

                    code = code.replace("[implements]", " implements " + source.getServiceSource().getShortName())
                            .replace("[extends]", "");
                    imports.add(source.getServiceSource().getClassFullName());
                } else {
                    code = code.replace("[implements]", "").replace("[extends]", "");
                }
            }
        } else {
            imports.add(ApplicationContant.config.getProperty("Autowired"));
            imports.add(ApplicationContant.config.getProperty("List"));
            imports.add(source.getEntitySource().getClassFullName());
            code = code.replace("[extends]", "");

            String fieldType = source.getMapperSource().getShortName();
            imports.add(source.getMapperSource().getClassFullName());
            code = generateComponentFields(code, fieldType);

            String templateCode = template.t_service_impl_methods_normal;
            if (source.isContainsPrimaryKey()) {
                templateCode = templateCode + "\n" + template.t_service_impl_methods_needprimarykey;
            }

            code = code.replace("[methods]", templateCode);
            code = code.replace("[mapperName]", CodeUtils.firstChar2Lowercase(source.getMapperSource().getShortName()));
            code = code.replace("[entityClass]", source.getEntitySource().getShortName());
            code = code.replace("[primaryKeyType]", getClassShortName(source.getEntitySource().getPrimaryKeyType()));
            if (source.getCreateInterface()) {
                imports.add(source.getServiceSource().getClassFullName());
                code = code.replace("[implements]", " implements " + source.getServiceSource().getShortName());
            } else {
                code = code.replace("[implements]", "");
                code = code.replace("@Override", "");
            }
        }


        if (source.getUseTransactional()) {
            imports.add(ApplicationContant.config.getProperty("Transactional"));
            anontations.add("Transactional(rollbackFor = Throwable.class)");
        }

        code = checkUseLombok(source, code, imports, anontations);

        code = generateImports(code, imports);
        code = generateAnnotations(code, anontations);
        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }

}
