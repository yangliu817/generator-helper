package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("t_java_type")
public class JavaType {
    private Long id;

    private String shortName;

    private String fullName;

    private boolean needImport = false;

}
