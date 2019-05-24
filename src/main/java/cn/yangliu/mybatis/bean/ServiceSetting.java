package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_service_setting")
@NoArgsConstructor
public class ServiceSetting {

    public ServiceSetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String servicePackage;

    private Boolean createInterface;

    private Boolean useBaseService;

    private Boolean useTransactional;

    private Boolean startWithI;

}
