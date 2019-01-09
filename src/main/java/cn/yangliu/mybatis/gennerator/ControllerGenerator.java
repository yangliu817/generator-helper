package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.source.ControllerSource;
import cn.yangliu.mybatis.source.ServiceImplSource;
import cn.yangliu.mybatis.source.ServiceSource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ControllerGenerator extends AbstractGenerator<ControllerSource> {

    @Override
    public void generate(ControllerSource source) {
        String code = template.t_controller;
        code = generateComments(code,source);
        String className = source.getShortName();
        code = code.replace("[className]", className);

        List<String> imports = new ArrayList<>();
        List<String> anontations = new ArrayList<>();

        if (source.getUseRestful()) {
            imports.add(ApplicationContant.config.getProperty("RestController"));
            anontations.add("RestController");
        } else {
            imports.add(ApplicationContant.config.getProperty("Controller"));
            anontations.add("Controller");
        }
        imports.add(ApplicationContant.config.getProperty("RequestMapping"));
        anontations.add("RequestMapping(\"/" + CodeUtils.firstChar2Lowercase(source.getEntitySource().getShortName()) + "\")");

        imports.add(ApplicationContant.config.getProperty("Autowired"));

        imports.add(source.getEntitySource().getClassFullName());

        String fieldType;

        ServiceImplSource serviceImplSource = source.getServiceImplSource();

        if (serviceImplSource.getCreateInterface()) {
            ServiceSource serviceSource = serviceImplSource.getServiceSource();
            imports.add(serviceSource.getClassFullName());
            fieldType = serviceSource.getShortName();
        } else {
            imports.add(serviceImplSource.getClassFullName());
            fieldType = serviceImplSource.getShortName();
        }
        code = generateComponentFields(code, fieldType);

        if (StringUtils.isEmpty(source.getFullPackage())) {
            code = code.replace("[package]", "");
        } else {
            code = code.replace("[package]", "package " + source.getFullPackage() + ";");
        }


        code = checkUseLombok(source, code, imports, anontations);

        String methodCode = generateMethod(source, imports, fieldType);

        code = code.replace("[methods]", methodCode);


        code = generateImports(code, imports);
        code = generateAnnotations(code, anontations);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }

    private String generateMethod(ControllerSource source, List<String> imports, String serviceType) {
        StringBuilder sb = new StringBuilder();
        sb.append(generateInsertMethod(source, imports, serviceType));
        sb.append(generateUpdateMethod(source, imports, serviceType));
        sb.append(generateListMethod(source, imports, serviceType));

        if (source.isContainsPrimaryKey()) {
            sb.append(generateGetByIdMethod(source, imports, serviceType));
            sb.append(generateDeleteMethed(source, imports, serviceType));
            imports.add(ApplicationContant.config.getProperty("PathVariable"));
        }
        if (source.getUseRestful()) {
            imports.add(ApplicationContant.config.getProperty("PutMapping"));
            if (source.isContainsPrimaryKey()) {
                imports.add(ApplicationContant.config.getProperty("DeleteMapping"));
            }
        }
        imports.add(ApplicationContant.config.getProperty("GetMapping"));
        imports.add(ApplicationContant.config.getProperty("PostMapping"));
        imports.add(ApplicationContant.config.getProperty("RequestParam"));


        if (!checkPackageIsSame(source.getFullPackage(), source.getMethodReturnTypeFullName())) {
            imports.add(source.getMethodReturnTypeFullName());
        }
        return sb.toString();
    }

    private String generateUpdateMethod(ControllerSource source, List<String> imports, String serviceType) {
        String updateMethodCode = template.t_controller_update;

        serviceType = CodeUtils.firstChar2Lowercase(serviceType);

        String entityClass = source.getEntitySource().getShortName();

        String returnType = getClassShortName(source.getMethodReturnTypeFullName());

        String returnInfo = getReturnInfo(source, imports, false);

        updateMethodCode = updateMethodCode.replace("[returnType]", returnType);

        if (source.getUseRestful()) {
            imports.add(ApplicationContant.config.getProperty("PutMapping"));
            updateMethodCode = updateMethodCode.replace("[annotation]", "PutMapping");
        } else {
            imports.add(ApplicationContant.config.getProperty("PostMapping"));
            updateMethodCode = updateMethodCode.replace("[annotation]", "PostMapping");
        }

        updateMethodCode = updateMethodCode.replace("[service]", serviceType);
        updateMethodCode = updateMethodCode.replace("[entityClass]", entityClass);
        updateMethodCode = updateMethodCode.replace("[returnInfo]", returnInfo);

        return updateMethodCode;
    }

    private String generateGetByIdMethod(ControllerSource source, List<String> imports, String serviceType) {

        serviceType = CodeUtils.firstChar2Lowercase(serviceType);
        String returnType = getClassShortName(source.getMethodReturnTypeFullName());
        String returnInfo = getReturnInfo(source, imports, true);
        String getByIdMethodCode = template.t_controller_getById;
        String entityClass = source.getEntitySource().getShortName();
        String primaryKeyType = source.getEntitySource().getPrimaryKeyType();

        if (!checkPackageIsSame(source.getFullPackage(), primaryKeyType)) {
            imports.add(primaryKeyType);
        }

        primaryKeyType = getClassShortName(primaryKeyType);

        getByIdMethodCode = getByIdMethodCode.replace("[returnType]", returnType)
                .replace("[service]", serviceType)
                .replace("[entityClass]", entityClass)
                .replace("[returnInfo]", returnInfo)
                .replace("[primaryKeyType]", primaryKeyType);

        String primaryKey = source.getEntitySource().getPrimaryKeyName();

        if (StringUtils.isNotEmpty(primaryKey)) {
            primaryKey = CodeUtils.getFieldName(primaryKey);
        } else {
            primaryKey = "id";
        }

        getByIdMethodCode = getByIdMethodCode.replace("[primaryKey]", primaryKey);

        return getByIdMethodCode;
    }

    private String generateListMethod(ControllerSource source, List<String> imports, String serviceType) {
        serviceType = CodeUtils.firstChar2Lowercase(serviceType);
        String returnType = getClassShortName(source.getMethodReturnTypeFullName());
        String entityClass = source.getEntitySource().getShortName();
        String returnInfo = getReturnInfo(source, imports, true);
        imports.add(ApplicationContant.config.getProperty("RequestParam"));

        String listMethodCode = template.t_controller_list;
        if (source.isMybatisPlus()) {
            imports.add(ApplicationContant.config.getProperty("Page"));
            imports.add(ApplicationContant.config.getProperty("EntityWrapper"));
        } else {
            listMethodCode = template.t_controller_list2;
            imports.add(ApplicationContant.config.getProperty("PagehelperPage"));
            imports.add(ApplicationContant.config.getProperty("PageHelper"));
        }

        listMethodCode = listMethodCode.replace("[returnType]", returnType)
                .replace("[entityClass]", entityClass)
                .replace("[service]", serviceType)
                .replace("[returnInfo]", returnInfo);

        return listMethodCode;
    }

    private String generateInsertMethod(ControllerSource source, List<String> imports, String serviceType) {
        String insertMethodCode = template.t_controller_insert;

        imports.add(ApplicationContant.config.getProperty("PostMapping"));

        String returnType = getClassShortName(source.getMethodReturnTypeFullName());

        insertMethodCode = insertMethodCode.replace("[returnType]", returnType);

        String entityClass = source.getEntitySource().getShortName();

        String service = CodeUtils.firstChar2Lowercase(serviceType);

        insertMethodCode = insertMethodCode.replace("[service]", service)
                .replace("[entityClass]", entityClass);
        imports.add(source.getEntitySource().getClassFullName());

        String returnInfo = getReturnInfo(source, imports, false);

        insertMethodCode = insertMethodCode.replace("[returnInfo]", returnInfo);

        return insertMethodCode;

    }


    private String generateDeleteMethed(ControllerSource source, List<String> imports, String serviceType) {

        String deleteMethodCode = template.t_controller_delete;

        imports.add(ApplicationContant.config.getProperty("RequestBody"));
        imports.add(ApplicationContant.config.getProperty("Arrays"));

        String returnType = getClassShortName(source.getMethodReturnTypeFullName());

        String service = CodeUtils.firstChar2Lowercase(serviceType);

        String primaryKeyType = source.getEntitySource().getPrimaryKeyType();

        if (!checkPackageIsSame(source.getFullPackage(), primaryKeyType)) {
            imports.add(primaryKeyType);
        }

        primaryKeyType = getClassShortName(primaryKeyType);

        deleteMethodCode = deleteMethodCode.replace("[primaryKeyType]", primaryKeyType);
        deleteMethodCode = deleteMethodCode.replace("[returnType]", returnType);
        deleteMethodCode = deleteMethodCode.replace("[service]", service);
        if (source.getUseRestful()) {
            imports.add(ApplicationContant.config.getProperty("DeleteMapping"));
            deleteMethodCode = deleteMethodCode.replace("[annotation]", "DeleteMapping");
        } else {
            imports.add(ApplicationContant.config.getProperty("PostMapping"));
            deleteMethodCode = deleteMethodCode.replace("[annotation]", "PostMapping");
        }
        String returnInfo = getReturnInfo(source, imports, false);

        deleteMethodCode = deleteMethodCode.replace("[returnInfo]", returnInfo);

        return deleteMethodCode;

    }

    private String getReturnInfo(ControllerSource source, List<String> imports, boolean flag) {
        String returnInfo = "null";
        if (Objects.equals(source.getMethodReturnTypeFullName(), "java.lang.String")) {
            return returnInfo;
        }
        if (Objects.equals(source.getMethodReturnTypeFullName(), ApplicationContant.config.getProperty("ModelAndView"))) {
            returnInfo = "new ModelAndView()";
            return returnInfo;
        }
        if (StringUtils.isEmpty(source.getMethodReturnTypeFullName())) {
            return returnInfo;
        }

        if (StringUtils.isEmpty(source.getReturnTypeStaticMethod())) {

            if (flag) {
                returnInfo = "new " + source.getMethodReturnTypeShortName() + "(data)";
                return returnInfo;
            }

            returnInfo = "new " + source.getMethodReturnTypeShortName();
            return returnInfo;
        }

        if (!checkPackageIsSame(source.getFullPackage(), source.getMethodReturnTypeFullName())) {
            imports.add(source.getMethodReturnTypeFullName());
        }
        returnInfo = source.getMethodReturnTypeShortName() + ApplicationContant.PACKAGE_SEPARATOR + source.getReturnTypeStaticMethod();


        if (flag) {
            returnInfo += "(data)";
            return returnInfo;
        }
        returnInfo += "()";

        return returnInfo;

    }

}
