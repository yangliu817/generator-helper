package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.ServiceSetting;
import lombok.Data;

import java.io.File;

@Data
public class ServiceImplSource extends AbstractCodeSource {

    private Boolean createInterface;

    private Boolean useBaseService;

    private Boolean useTransactional;

    private ServiceSource serviceSource;

    private MapperSource mapperSource;

    private RepositorySource repositorySource;

    private EntitySource entitySource;

    public ServiceImplSource(ProjectSetting projectSetting, ServiceSetting serviceSetting,
                             RepositorySource repositorySource, MapperSource mapperSource) {
        super(projectSetting, serviceSetting.getServicePackage(), projectSetting.getCodePath());

        this.mapperSource = mapperSource;
        this.repositorySource = repositorySource;
        this.useTransactional = serviceSetting.getUseTransactional();
        this.entitySource = mapperSource.getEntitySource();
        if (serviceSetting.getCreateInterface()) {
            serviceSource = new ServiceSource(projectSetting, serviceSetting, entitySource,mapperSource);
        }
        init(serviceSetting, entitySource.shortName, projectSetting.getCodePath());
    }

    private void init(ServiceSetting serviceSetting, String entityName, String srcPath) {
        setPrimaryKeyInfo(entitySource);
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

        classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;

        filepath = filepath.replace(ApplicationContant.PACKAGE_SEPARATOR, File.separator);

    }


}
