package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName
public class SqliteSequence {

    private String name;

    private Long seq;

    public <T> SqliteSequence(T entity) {
        Class<?> aClass = entity.getClass();
        TableName tablename = aClass.getDeclaredAnnotation(TableName.class);
        this.name = tablename.value();
    }
}
