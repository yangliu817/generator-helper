package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_controller_setting")
@NoArgsConstructor
public class ControllerSetting {

    public ControllerSetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String controllerPackage;

    private String methodReturnTypeFullName;

    private String returnTypeStaticMethod;

    private Boolean useRestful;

    private Boolean forceIdOperate;

}
