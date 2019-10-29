package cn.yangliu.mybatis.enums;

/**
 * The enum Db type enum.
 *
 * @author 杨柳
 * @date 2019 -01-06
 */
public enum DBTypeEnum{
    /**
     * Mysql db type enum.
     */
    MYSQL("mysql"),
    /**
     * Oracle db type enum.
     */
    ORACLE("oracle"),
    /**
     * Mariadb db type enum.
     */
    MARIADB("mariadb"),
    /**
     * Sqlserver db type enum.
     */
    SQLSERVER("sqlserver"),
    ;
    private String dbType;

    DBTypeEnum(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Gets db type.
     *
     * @return the db type
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Sets db type.
     *
     * @param dbType the db type
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
