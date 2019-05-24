package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.ServiceSetting;
import lombok.Data;

@Data
public class ServiceSource extends AbstractCodeSource {

    private Boolean useBaseService;

    private EntitySource entitySource;

    private MapperSource mapperSource;

    private Boolean startWithI;

    public ServiceSource(ProjectSetting projectSetting, ServiceSetting serviceSetting, EntitySource entitySource, MapperSource mapperSource) {
        super(projectSetting, serviceSetting.getServicePackage(), projectSetting.getCodePath());
        this.entitySource = entitySource;
        this.mapperSource = mapperSource;
        init(serviceSetting, entitySource.shortName);
    }


    private void init(ServiceSetting serviceSetting, String entityName) {
        setPrimaryKeyInfo(entitySource);
        this.startWithI = serviceSetting.getStartWithI();
        useBaseService = serviceSetting.getUseBaseService();
        shortName = entityName + "Service";
        filename = shortName + ".java";
        if (serviceSetting.getStartWithI()) {
            shortName = "I" + entityName + "Service";
            filename = shortName + ".java";
        }


        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;
        } else {
            classFullName = shortName;
        }
    }

}
