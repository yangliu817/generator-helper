package cn.yangliu.mybatis.enums;

/**
 * @author 杨柳
 * @date 2019-01-06
 */
public enum DBTypeEnum{
    /**
     *
     */
    MYSQL("mysql"),
    ORACLE("oracle"),
    MARIADB("mariadb"),
    SQLSERVER("sqlserver"),
    ;
    private String dbType;

    DBTypeEnum(String dbType) {
        this.dbType = dbType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
