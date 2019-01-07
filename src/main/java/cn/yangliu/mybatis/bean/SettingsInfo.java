package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@TableName("t_settings_info")
@ToString
public class SettingsInfo implements Serializable {

    @TableId
    private Long id;

    private Long linkId;

    private String name;

    @TableField(exist = false)
    private Settings settings;

    @TableField(exist = false)
    private String dbType;

}
