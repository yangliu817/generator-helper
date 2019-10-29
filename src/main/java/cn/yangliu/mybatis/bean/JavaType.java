package cn.yangliu.mybatis.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * The type Java type.
 */
@Data
@TableName("t_java_type")
@NoArgsConstructor
public class JavaType {

    /**
     * Instantiates a new Java type.
     *
     * @param shortName  the short name
     * @param fullName   the full name
     * @param needImport the need import
     */
    public JavaType(String shortName, String fullName, boolean needImport) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.needImport = needImport;
    }

    /**
     * The constant DEFAULT.
     */
    public static JavaType DEFAULT = new JavaType("String", "java.lang.String", false);

    private Long id;

    private String shortName;

    private String fullName;

    private boolean needImport = false;

    /**
     * New instance java type.
     *
     * @param javaType the java type
     * @return the java type
     */
    public static JavaType newInstance(JavaType javaType){
        String shortName = javaType.shortName;
        String fullName = javaType.fullName;
        boolean needInport = javaType.needImport;

        return new JavaType(shortName,fullName,needInport);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JavaType javaType = (JavaType) o;
        return needImport == javaType.needImport &&
                Objects.equals(shortName, javaType.shortName) &&
                Objects.equals(fullName, javaType.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shortName, fullName, needImport);
    }
}
