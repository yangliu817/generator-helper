package cn.yangliu.mybatis.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Mapper setting.
 */
@Data
@TableName("t_mapper_setting")
@NoArgsConstructor
public class MapperSetting {

    /**
     * Instantiates a new Mapper setting.
     *
     * @param settingId the setting id
     */
    public MapperSetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String mapperPackage;

    private String mapperSufix;

    private Boolean extendBaseMapper;

    private Boolean useMapperAnonntation;


}
