package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@TableName("t_entity_setting")
@NoArgsConstructor
public class EntitySetting {

    public EntitySetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String entityPackage;

    private String baseClassFullName;

    private Boolean equalAndHash;

    private Boolean toString;

    private Boolean noArgConstructor;

    private Boolean chain;

    private Boolean useBaseClass;

    private String primaryKeyName;

    private String excludeColumns;

    private String primaryKeyType;

    @TableField(exist = false)
    private List<MappingSetting> mapping;


}
