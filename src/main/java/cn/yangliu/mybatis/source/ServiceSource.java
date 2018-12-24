package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.ServiceSetting;
import lombok.Data;

@Data
public class ServiceSource extends CodeSource {

    private Boolean useBaseService;

    private EntitySource entitySource;

    public ServiceSource(ProjectSetting projectSetting, ServiceSetting serviceSetting, EntitySource entitySource) {
        super(projectSetting, serviceSetting.getServicePackage(),projectSetting.getCodePath());
        init(serviceSetting, entitySource.shortName);
        this.entitySource = entitySource;
    }


    private void init(ServiceSetting serviceSetting, String entityName) {
        useBaseService = serviceSetting.getUseBaseService();
        shortName = entityName + "Service";
        filename = shortName + ".java";
        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + "." + shortName;
        } else {
            classFullName = shortName;
        }
    }

}
