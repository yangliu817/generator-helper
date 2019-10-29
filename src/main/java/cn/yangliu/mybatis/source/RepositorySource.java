package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.bean.RepositorySetting;
import lombok.Data;

/**
 * The type Repository source.
 */
@Data
public class RepositorySource extends AbstractCodeSource {

    private Boolean useRepositoryAnonntation;

    private EntitySource entitySource;

    /**
     * Instantiates a new Repository source.
     *
     * @param projectSetting    the project setting
     * @param repositorySetting the repository setting
     * @param entitySource      the entity source
     */
    public RepositorySource(ProjectSetting projectSetting, RepositorySetting repositorySetting, EntitySource entitySource) {
        super(projectSetting, repositorySetting.getRepositoryPackage(), projectSetting.getCodePath());
        this.entitySource = entitySource;
        init(repositorySetting, entitySource.shortName.replace(entitySource.getClassSufix(),""));
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
