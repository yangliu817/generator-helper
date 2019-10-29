package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Project setting.
 */
@Data
@TableName("t_project_setting")
@NoArgsConstructor
public class ProjectSetting {

    /**
     * Instantiates a new Project setting.
     *
     * @param settingId the setting id
     */
    public ProjectSetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String projectPackage;

    private String tablePrefix;

    private String codePath;

    private Boolean useLombok;

    private Boolean useSwagger;

    private Boolean createService;

    private Boolean createController;

    private Integer ormType;

    /**
     * The Copyright.
     */
    protected String copyright;

    /**
     * The Author.
     */
    protected String author;

    /**
     * The Use shiro.
     */
    protected Boolean useShiro;

}
