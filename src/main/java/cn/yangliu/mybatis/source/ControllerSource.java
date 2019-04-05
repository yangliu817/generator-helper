package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ControllerSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.ex.HelperException;
import lombok.Data;

@Data
public class ControllerSource extends AbstractCodeSource {

    private String methodReturnTypeFullName;

    private String methodReturnTypeShortName;

    private String methodReturnTypePackage;

    private String returnTypeStaticMethod;

    private Boolean useRestful;

    private ServiceImplSource serviceImplSource;

    private EntitySource entitySource;

    private Boolean useShiro;

    public ControllerSource(ProjectSetting projectSetting, ControllerSetting controllerSetting, ServiceImplSource serviceImplSource) {
        super(projectSetting, controllerSetting.getControllerPackage(), projectSetting.getCodePath());
        this.serviceImplSource = serviceImplSource;
        this.useShiro = projectSetting.getUseShiro();
        entitySource = serviceImplSource.getEntitySource();
        init(controllerSetting, entitySource.shortName);
    }

    private void init(ControllerSetting controllerSetting, String entityName) {
        setPrimaryKeyInfo(entitySource);
        useRestful = controllerSetting.getUseRestful();
        methodReturnTypeFullName = controllerSetting.getMethodReturnTypeFullName();

        if (useRestful && StringUtils.isEmpty(methodReturnTypeFullName)) {
            methodReturnTypeFullName = "java.lang.String";
        }

        if (StringUtils.isEmpty(methodReturnTypeFullName)) {
            methodReturnTypeFullName = ApplicationContant.config.getProperty("ModelAndView");
        }

        if (methodReturnTypeFullName.endsWith(ApplicationContant.PACKAGE_SEPARATOR)) {
            throw new HelperException("不合法");
        }

        if (methodReturnTypeFullName.contains(ApplicationContant.PACKAGE_SEPARATOR)) {
            methodReturnTypeShortName = methodReturnTypeFullName.substring(methodReturnTypeFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR) + 1);
            methodReturnTypePackage = methodReturnTypeFullName.substring(0, methodReturnTypeFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR));
        } else {
            methodReturnTypeShortName = methodReturnTypeFullName;
            methodReturnTypePackage = "";
        }


        returnTypeStaticMethod = controllerSetting.getReturnTypeStaticMethod();
        shortName = entityName + "Controller";
        filename = shortName + ".java";


        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;
        } else {
            classFullName = shortName;
        }
    }


}
