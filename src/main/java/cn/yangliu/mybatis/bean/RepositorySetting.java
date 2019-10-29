package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Repository setting.
 */
@Data
@TableName("t_repository_setting")
@NoArgsConstructor
public class RepositorySetting {

    /**
     * Instantiates a new Repository setting.
     *
     * @param settingId the setting id
     */
    public RepositorySetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String repositoryPackage;

    private String repositorySufix;

    private Boolean useRepositoryAnonntation;

}
