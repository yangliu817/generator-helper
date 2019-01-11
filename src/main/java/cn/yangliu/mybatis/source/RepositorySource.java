package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.RepositorySetting;
import lombok.Data;

@Data
public class RepositorySource extends CodeSource {

    private Boolean useRepositoryAnonntation;

    private EntitySource entitySource;

    public RepositorySource(ProjectSetting projectSetting, RepositorySetting repositorySetting, EntitySource entitySource) {
        super(projectSetting, repositorySetting.getRepositoryPackage(), projectSetting.getCodePath());
        this.entitySource = entitySource;
        init(repositorySetting, entitySource.shortName);
    }

    private void init(RepositorySetting repositorySetting, String entityName) {
        setPrimaryKeyInfo(entitySource);
        String repositorySufix = repositorySetting.getRepositorySufix();
        useRepositoryAnonntation = repositorySetting.getUseRepositoryAnonntation();

        shortName = entityName + repositorySufix.trim();

        filename = shortName + ".java";
        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;
        } else {
            classFullName = shortName;
        }
    }
}
