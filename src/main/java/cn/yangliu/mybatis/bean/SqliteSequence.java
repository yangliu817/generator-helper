package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Sqlite sequence.
 */
@Data
@NoArgsConstructor
@TableName
public class SqliteSequence {

    private String name;

    private Long seq;

    /**
     * Instantiates a new Sqlite sequence.
     *
     * @param entity the entity
     */
    public <T> SqliteSequence(T entity) {
        Class<?> aClass = entity.getClass();
        TableName tablename = aClass.getDeclaredAnnotation(TableName.class);
        this.name = tablename.value();
    }
}
