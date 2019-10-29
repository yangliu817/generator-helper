package cn.yangliu.mybatis.enums;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * The enum Strategy enum.
 *
 * @author 杨柳
 * @date 2019 -01-12
 */
public enum StrategyEnum {
    /**
     * Jpa table strategy enum.
     */
    JPA_TABLE("TABLE", "31"),
    /**
     * Jpa sequence strategy enum.
     */
    JPA_SEQUENCE("SEQUENCE", "32"),
    /**
     * Jpa identity strategy enum.
     */
    JPA_IDENTITY("IDENTITY", "33"),
    /**
     * Jpa auto strategy enum.
     */
    JPA_AUTO("AUTO", "34"),
    /**
     * Mp auto strategy enum.
     */
    MP_AUTO("AUTO", "20"),
    /**
     * Mp input strategy enum.
     */
    MP_INPUT("INPUT", "21"),
    /**
     * Mp id worker strategy enum.
     */
    MP_ID_WORKER("ID_WORKER", "22"),
    /**
     * Mp uuid strategy enum.
     */
    MP_UUID("UUID", "23"),
    /**
     * Mp none strategy enum.
     */
    MP_NONE("NONE", "24"),
    /**
     * Mp id worker str strategy enum.
     */
    MP_ID_WORKER_STR("ID_WORKER_STR", "25"),
    ;
    private String name;

    private String strategy;

    StrategyEnum(String name, String strategy) {
        this.name = name;
        this.strategy = strategy;
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
     * Gets strategy.
     *
     * @return the strategy
     */
    public String getStrategy() {
        return strategy;
    }

    /**
     * Sets strategy.
     *
     * @param strategy the strategy
     */
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    /**
     * Gets enum by strategy.
     *
     * @param strategy the strategy
     * @return the enum by strategy
     */
    public static StrategyEnum getEnumByStrategy(String strategy) {
        Optional<StrategyEnum> optional = Arrays.stream(values()).filter(s -> Objects.equals(s.strategy, strategy)).findAny();
        Assert.isTrue(optional.isPresent(), "不支持的主键策略");
        return optional.get();
    }
}
