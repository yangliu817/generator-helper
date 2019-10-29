package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Objects;

/**
 * The type Column type.
 */
@Data
@TableName("t_column_type")
public class ColumnType {

    @TableId
    private Long id;

    private String columnType;

    private String dbType;

}
