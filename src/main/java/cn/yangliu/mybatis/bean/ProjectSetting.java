package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_project_setting")
@NoArgsConstructor
public class ProjectSetting {

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

    private Boolean createService;

    private Boolean createController;

    private Integer mybatisType;

}
