package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_mapping_setting")
@NoArgsConstructor
public class MappingSetting {

    public MappingSetting(Long settingId, String dbType) {
        this.settingId = settingId;
        this.dbType = dbType;
    }

    public MappingSetting(Long columnTypeId, Long javaTypeId, Long settingId) {
        this.columnTypeId = columnTypeId;
        this.javaTypeId = javaTypeId;
        this.settingId = settingId;
    }

    @TableId
    private Long id;

    private Long columnTypeId;

    private Long javaTypeId;

    private Long settingId;

    @TableField(exist = false)
    private String columnType;

    @TableField(exist = false)
    private String fullName;

    @TableField(exist = false)
    private String dbType;


}
