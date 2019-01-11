package cn.yangliu.mybatis.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author 杨柳
 * @date 2019-01-11
 */
public enum OrmTypeEnum {
    /***/
    MybatisPlus("mybatis-plus", 2),
    JPA("jpa", 3),
    Mybatis("Mybatis", 1);

    private String name;

    private int type;

    OrmTypeEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static OrmTypeEnum getOrmTypeEnumByType(int type) {
        OrmTypeEnum[] values = OrmTypeEnum.values();
        Optional<OrmTypeEnum> ormTypeEnumOptional = Arrays.stream(values).filter(t -> t.type == type).findAny();
        if (ormTypeEnumOptional.isPresent()) {
            return ormTypeEnumOptional.get();
        }
        throw new IllegalArgumentException("不匹配的类型");
    }
}

