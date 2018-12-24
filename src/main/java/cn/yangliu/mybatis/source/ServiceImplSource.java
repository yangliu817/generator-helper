package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.ServiceSetting;
import lombok.Data;

import java.io.File;

@Data
public class ServiceImplSource extends CodeSource {

    private Boolean createInterface;

    private Boolean useBaseService;

    private Boolean useTransactional;

    private ServiceSource serviceSource;

    private MapperSource mapperSource;

    private EntitySource entitySource;

    public ServiceImplSource(ProjectSetting projectSetting, ServiceSetting serviceSetting, MapperSource mapperSource) {
        super(projectSetting, serviceSetting.getServicePackage(), projectSetting.getCodePath());

        this.mapperSource = mapperSource;
        this.useTransactional = serviceSetting.getUseTransactional();
        this.entitySource = mapperSource.getEntitySource();
        if (serviceSetting.getCreateInterface()) {
            serviceSource = new ServiceSource(projectSetting, serviceSetting, entitySource);
        }
        init(serviceSetting, entitySource.shortName, projectSetting.getCodePath());
    }

    private void init(ServiceSetting serviceSetting, String entityName, String srcPath) {
        createInterface = serviceSetting.getCreateInterface();
        useBaseService = serviceSetting.getUseBaseService();
        if (serviceSetting.getCreateInterface()) {
            shortName = entityName + "ServiceImpl";
            fullPackage += ".impl";
            filename = shortName + ".java";

        } else {
            shortName = entityName + "Service";
            filename = shortName + ".java";
        }

        if (srcPath.endsWith("/") || srcPath.endsWith("\\")) {
            filepath = srcPath + "src" + File.separator + "main" + File.separator + "java" + File.separator + fullPackage;
        } else {
            filepath = srcPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + fullPackage;
        }

        classFullName = fullPackage + "." + shortName;

        filepath = filepath.replace(".", File.separator);

    }


}
