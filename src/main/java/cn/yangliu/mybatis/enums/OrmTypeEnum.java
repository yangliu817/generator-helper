package cn.yangliu.mybatis.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * The enum Orm type enum.
 *
 * @author 杨柳
 * @date 2019 -01-11
 */
public enum OrmTypeEnum {
    /**
     * Mybatis plus orm type enum.
     */
    MybatisPlus("mybatis-plus", 2),
    /**
     * Jpa orm type enum.
     */
    JPA("jpa", 3),
    /**
     * Mybatis orm type enum.
     */
    Mybatis("Mybatis", 1);

    private String name;

    private int type;

    OrmTypeEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Gets orm type enum by type.
     *
     * @param type the type
     * @return the orm type enum by type
     */
    public static OrmTypeEnum getOrmTypeEnumByType(int type) {
        OrmTypeEnum[] values = OrmTypeEnum.values();
        Optional<OrmTypeEnum> ormTypeEnumOptional = Arrays.stream(values).filter(t -> t.type == type).findAny();
        if (ormTypeEnumOptional.isPresent()) {
            return ormTypeEnumOptional.get();
        }
        throw new IllegalArgumentException("不匹配的类型");
    }
}

