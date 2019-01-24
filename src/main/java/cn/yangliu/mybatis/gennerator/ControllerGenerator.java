package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
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
        code = generatePackage(source, code);
        code = generateComments(code, source);
        code = code.replace("[className]", source.getShortName());

        List<String> imports = new ArrayList<>();
        List<String> anontations = new ArrayList<>();
        String swaggerDesp = source.getEntitySource().getShortName() + "操作接口";
        if (source.getUseSwagger()) {
            imports.add(ApplicationContant.config.getProperty("Api"));
            anontations.add("Api(value = \"" + swaggerDesp + "\", tags = \"" + swaggerDesp + "\", description = \"" + swaggerDesp + "\")");
        }
        String controllerAnnotationFullName = ApplicationContant.config.getProperty("Controller");
        String anontation = "Controller";
        if (source.getUseRestful()) {
            controllerAnnotationFullName = ApplicationContant.config.getProperty("RestController");
            anontation = "RestController";
        }
        imports.add(controllerAnnotationFullName);
        anontations.add(anontation);

        imports.add(ApplicationContant.config.getProperty("RequestMapping"));
        anontations.add("RequestMapping(\"/" + CodeUtils.firstChar2Lowercase(source.getEntitySource().getShortName()) + "\")");

        imports.add(ApplicationContant.config.getProperty("Autowired"));

        imports.add(source.getEntitySource().getClassFullName());

        //引用service
        ServiceImplSource serviceImplSource = source.getServiceImplSource();
        String fieldType = serviceImplSource.getShortName();
        String serverClassFullName = serviceImplSource.getClassFullName();
        if (serviceImplSource.getCreateInterface()) {
            ServiceSource serviceSource = serviceImplSource.getServiceSource();
            serverClassFullName = serviceSource.getClassFullName();
            fieldType = serviceSource.getShortName();
        }
        imports.add(serverClassFullName);
        code = generateComponentFields(code, fieldType);

        code = checkUseLombok(source, code, imports, anontations);

        String methodCode = generateMethod(source, imports, fieldType, swaggerDesp);

        code = code.replace("[methods]", methodCode);

        code = generateImports(code, imports);
        code = generateAnnotations(code, anontations);

        FileUtils.output(code, source.getFilepath(), source.getFilename());
    }


    private String generateMethod(ControllerSource source, List<String> imports, String serviceType, String swaggerDesp) {
        StringBuilder sb = new StringBuilder();

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            sb.append(generateSaveMethod(source, imports,swaggerDesp));
        } else {
            sb.append(generateInsertMethod(source, imports,swaggerDesp));
            sb.append(generateUpdateMethod(source, imports,swaggerDesp));
        }

        sb.append(generateListMethod(source, imports,swaggerDesp));

        if (source.isContainsPrimaryKey()) {
            sb.append(generateGetByIdMethod(source, imports,swaggerDesp));
            sb.append(generateDeleteMethed(source, imports,swaggerDesp));
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

        String returnType = getClassShortName(source.getMethodReturnTypeFullName());

        String service = CodeUtils.firstChar2Lowercase(serviceType);

        String primaryKeyType = source.getEntitySource().getPrimaryKeyType();

        if (!checkPackageIsSame(source.getFullPackage(), primaryKeyType)) {
            imports.add(primaryKeyType);
        }

        primaryKeyType = getClassShortName(primaryKeyType);

        String methodCode = sb.toString();

        methodCode = methodCode.replace("[primaryKeyType]", primaryKeyType);
        methodCode = methodCode.replace("[returnType]", returnType);
        methodCode = methodCode.replace("[service]", service);
        methodCode = methodCode.replace("[entityClass]", source.getEntitySource().getShortName());
        return methodCode;
    }

    private String generateUpdateMethod(ControllerSource source, List<String> imports,String swaggerDesp) {
        String updateMethodCode = template.t_controller_update;

        String returnInfo = getReturnInfo(source, imports, false);

        String importCode = ApplicationContant.config.getProperty("PostMapping");
        String annotationCode = "PostMapping";

        String httpMethod = "POST";
        if (source.getUseRestful()) {
            importCode = ApplicationContant.config.getProperty("PutMapping");
            annotationCode = "PutMapping";
            httpMethod = "PUT";
        }
        imports.add(importCode);
        updateMethodCode = updateMethodCode.replace("[annotation]", annotationCode);

        updateMethodCode = updateMethodCode.replace("[returnInfo]", returnInfo);

        String methodDesp = "修改" + source.getEntitySource().getShortName();

        updateMethodCode = generateSwaggerAnnotation(methodDesp,httpMethod,updateMethodCode,swaggerDesp,imports,source);


        return updateMethodCode;
    }

    private String generateGetByIdMethod(ControllerSource source, List<String> imports, String swaggerDesp) {
        String returnInfo = getReturnInfo(source, imports, true);
        String getByIdMethodCode = template.t_controller_getById;
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            getByIdMethodCode = template.t_controller_getById_jpa;
        }
        getByIdMethodCode = getByIdMethodCode.replace("[returnInfo]", returnInfo);

        String primaryKey = source.getEntitySource().getPrimaryKeyName();

        if (StringUtils.isNotEmpty(primaryKey)) {
            primaryKey = CodeUtils.getFieldName(primaryKey);
        } else {
            primaryKey = "id";
        }

        getByIdMethodCode = getByIdMethodCode.replace("[primaryKey]", primaryKey);

        String methodDesp = "通过主键查询" + source.getEntitySource().getShortName();
        getByIdMethodCode = generateSwaggerAnnotation(methodDesp,"GET",getByIdMethodCode,swaggerDesp,imports,source);

        return getByIdMethodCode;
    }

    private String generateSwaggerAnnotation(String methodDesp, String httpMethod, String methodCode,
                                             String swaggerDesp, List<String> imports,
                                             ControllerSource source) {
        if (source.getUseSwagger()) {
            imports.add(ApplicationContant.config.getProperty("ApiOperation"));
            String swaggerAnnotation = "@ApiOperation(value = \"" + methodDesp + "\", httpMethod = \"" + httpMethod + "\", " +
                    "notes = \"" + methodDesp + "\", tags = \"" + swaggerDesp + "\"";
            if (source.getUseRestful()) {
                swaggerAnnotation = swaggerAnnotation + ", response = " + source.getMethodReturnTypeShortName() + ".class";
            }
            swaggerAnnotation = swaggerAnnotation + ")";
            return methodCode.replace("[swaggerAnnotation]", swaggerAnnotation);
        }

        methodCode = methodCode.replace("    [swaggerAnnotation]\n", "");

        return methodCode;

    }

    private String generateListMethod(ControllerSource source, List<String> imports, String swaggerDesp) {
        String returnInfo = getReturnInfo(source, imports, true);
        imports.add(ApplicationContant.config.getProperty("RequestParam"));

        String listMethodCode = template.t_controller_list;
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)) {
            imports.add(ApplicationContant.config.getProperty("Page"));
            imports.add(ApplicationContant.config.getProperty("EntityWrapper"));
        }
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis)) {
            listMethodCode = template.t_controller_list2;
            imports.add(ApplicationContant.config.getProperty("PagehelperPage"));
            imports.add(ApplicationContant.config.getProperty("PageHelper"));
        }

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            listMethodCode = template.t_controller_list_jpa;
            imports.add(ApplicationContant.config.getProperty("PageJpa"));
            imports.add(ApplicationContant.config.getProperty("PageRequest"));
        }
        listMethodCode = listMethodCode.replace("[returnInfo]", returnInfo);

        String methodDesp = "列表查询" + source.getEntitySource().getShortName();
        listMethodCode = generateSwaggerAnnotation(methodDesp,"GET",listMethodCode,swaggerDesp,imports,source);

        return listMethodCode;
    }

    private String generateSaveMethod(ControllerSource source, List<String> imports, String swaggerDesp) {
        String saveMethodCode = template.t_controller_save_jpa;
        imports.add(ApplicationContant.config.getProperty("PostMapping"));
        String returnInfo = getReturnInfo(source, imports, false);
        imports.add(source.getEntitySource().getClassFullName());
        saveMethodCode = saveMethodCode.replace("[returnInfo]", returnInfo);

        saveMethodCode = generateSwaggerAnnotation("新增或修改" + source.getEntitySource().getShortName(),
                "POST",saveMethodCode,swaggerDesp,imports,source);

        return saveMethodCode;
    }

    private String generateInsertMethod(ControllerSource source, List<String> imports,String swaggerDesp) {
        String insertMethodCode = template.t_controller_insert;
        imports.add(ApplicationContant.config.getProperty("PostMapping"));
        imports.add(source.getEntitySource().getClassFullName());
        String returnInfo = getReturnInfo(source, imports, false);
        insertMethodCode = insertMethodCode.replace("[returnInfo]", returnInfo);

        String methodDesp = "新增" + source.getEntitySource().getShortName();
        insertMethodCode = generateSwaggerAnnotation(methodDesp,"POST",insertMethodCode,swaggerDesp,imports,source);

        return insertMethodCode;

    }


    private String generateDeleteMethed(ControllerSource source, List<String> imports,String swaggerDesp) {

        String deleteMethodCode = template.t_controller_delete;

        if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            deleteMethodCode = template.t_controller_delete_jpa;
        } else {
            imports.add(ApplicationContant.config.getProperty("Arrays"));
        }

        imports.add(ApplicationContant.config.getProperty("RequestBody"));

        String httpMethod = "POST";

        if (source.getUseRestful()) {
            httpMethod = "DELETE";
            imports.add(ApplicationContant.config.getProperty("DeleteMapping"));
            deleteMethodCode = deleteMethodCode.replace("[annotation]", "DeleteMapping");
        } else {
            imports.add(ApplicationContant.config.getProperty("PostMapping"));
            deleteMethodCode = deleteMethodCode.replace("[annotation]", "PostMapping");
        }
        String returnInfo = getReturnInfo(source, imports, false);

        deleteMethodCode = deleteMethodCode.replace("[returnInfo]", returnInfo);

        String methodDesp = "通过主键删除" + source.getEntitySource().getShortName();

        deleteMethodCode = generateSwaggerAnnotation(methodDesp,httpMethod,deleteMethodCode,swaggerDesp,imports,source);

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
