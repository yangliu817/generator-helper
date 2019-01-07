package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.ServiceSetting;
import lombok.Data;

@Data
public class ServiceSource extends CodeSource {

    private Boolean useBaseService;

    private EntitySource entitySource;

    public ServiceSource(ProjectSetting projectSetting, ServiceSetting serviceSetting, EntitySource entitySource) {
        super(projectSetting, serviceSetting.getServicePackage(),projectSetting.getCodePath());
        this.entitySource = entitySource;
        init(serviceSetting, entitySource.shortName);
    }


    private void init(ServiceSetting serviceSetting, String entityName) {
        setPrimaryKeyInfo(entitySource);
        useBaseService = serviceSetting.getUseBaseService();
        shortName = entityName + "Service";
        filename = shortName + ".java";
        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;
        } else {
            classFullName = shortName;
        }
    }

}
