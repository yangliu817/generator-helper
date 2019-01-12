package cn.yangliu.mybatis.enums;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author 杨柳
 * @date 2019-01-12
 */
public enum StrategyEnum {
    /***/
    JPA_TABLE("TABLE", "31"),
    JPA_SEQUENCE("SEQUENCE", "32"),
    JPA_IDENTITY("IDENTITY", "33"),
    JPA_AUTO("AUTO", "34"),
    MP_AUTO("AUTO", "20"),
    MP_INPUT("INPUT", "21"),
    MP_ID_WORKER("ID_WORKER", "22"),
    MP_UUID("UUID", "23"),
    MP_NONE("NONE", "24"),
    MP_ID_WORKER_STR("ID_WORKER_STR", "25"),
    ;
    private String name;

    private String strategy;

    StrategyEnum(String name, String strategy) {
        this.name = name;
        this.strategy = strategy;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public static StrategyEnum getEnumByStrategy(String strategy) {
        Optional<StrategyEnum> optional = Arrays.stream(values()).filter(s -> Objects.equals(s.strategy, strategy)).findAny();
        Assert.isTrue(optional.isPresent(), "不支持的主键策略");
        return optional.get();
    }
}
