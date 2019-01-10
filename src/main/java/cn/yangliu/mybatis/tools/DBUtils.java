package cn.yangliu.mybatis.tools;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.enums.DBTypeEnum;
import lombok.Data;
import org.apache.ibatis.io.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DBUtils {

    public static void init() {
        try {
            String db_path = "db";
            File file = new File(db_path);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(db_path + "/sqlite.db");
            if (file.exists()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");
            //建立一个数据库名sqlite.db的连接，如果不存在就在当前目录下创建之
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db/sqlite.db");
            Statement statement = conn.createStatement();

            String initSql = getInitSql();
            //建表语句
            statement.executeUpdate(initSql);

            close(statement, conn);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    private static String getInitSql() {
        StringBuilder sb = new StringBuilder();

        try (InputStream is = Resources.getResourceAsStream("init.sql");
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return sb.toString();
    }

    private static DBType getDBType(LinkInfo linkInfo) {
        String url = null;
        String driver = null;
        switch (linkInfo.getDatabaseType()) {
            case "mysql":
                url = "jdbc:mysql://" + linkInfo.getHost() + ":" + linkInfo.getPort() + "?useSSL=false";
                driver = "com.mysql.jdbc.Driver";
                break;
            case "mariadb":
                url = "jdbc:mysql://" + linkInfo.getHost() + ":" + linkInfo.getPort() + "?useSSL=false";
                driver = "com.mysql.jdbc.Driver";
                break;
            case "oracle":
                url = "jdbc:oracle:thin:@//" + linkInfo.getHost() + ":" + linkInfo.getPort() + "/" + linkInfo.getService();
                driver = "oracle.jdbc.driver.OracleDriver";
                break;
            case "sqlserver":
                url = "jdbc:sqlserver://" + linkInfo.getHost() + ":" + linkInfo.getPort();
                if (StringUtils.isNotEmpty(linkInfo.getDatabase())) {
                    url = url + ";DatabaseName=" + linkInfo.getDatabase();
                }
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                break;
            default:
                throw new NullPointerException("不支持的数据库类型");
        }
        return new DBType(url, driver);
    }


    @Data
    static class DBType {
        private String url;
        private String driver;

        public DBType(String url, String driver) {
            this.url = url;
            this.driver = driver;
        }
    }


    private static <T> List<T> excuteSQL(String sql, LinkInfo linkInfo, BiConsumer<ResultSet, List<T>> consumer) {
        List<T> dataList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = getConnection(linkInfo);
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                consumer.accept(rs, dataList);
            }
            return dataList;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, ps, connection);
        }
    }

    public static List<DatabaseInfo> getDatabases(LinkInfo linkInfo) {
        String sql = "";
        BiConsumer<ResultSet, List<DatabaseInfo>> consumer = (rs, list) -> {
            try {
                String databaseName = rs.getString(1);
                list.add(new DatabaseInfo(databaseName));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
        if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.ORACLE.getDbType())) {
            return Arrays.asList(new DatabaseInfo(linkInfo.getUser().toUpperCase()));
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.MYSQL.getDbType())) {
            sql = "show databases";
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.MARIADB.getDbType())) {
            sql = "show databases";
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.SQLSERVER.getDbType())) {
            sql = "SELECT name FROM sys.databases WHERE HAS_DBACCESS(name) = 1 and name not in ('master','tempdb','msdb')";
        }

        return excuteSQL(sql, linkInfo, consumer);
    }


    private static void close(AutoCloseable... sources) {
        if (sources == null || sources.length == 0) {
            return;
        }
        for (AutoCloseable source : sources) {
            if (source == null) {
                continue;
            }
            try {
                source.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Data
    public static class DatabaseInfo {

        private String name;

        public DatabaseInfo(String name) {
            this.name = name;
        }

        private List<TableInfo> tables;

        private Boolean checked = false;
    }

    @Data
    public static class ColumInfo {

        private String name;

        private String type;

        private String comment;

        private int index;

        public ColumInfo(String name, String type, String comment, int index) {
            this.name = name;
            this.type = type;
            this.comment = comment;
            this.index = index;
        }
    }

    @Data
    public static class TableInfo {

        private String name;

        private Boolean checked = false;

        private String comment;

        private List<ColumInfo> columInfos;

        public TableInfo(String name, String comment, List<ColumInfo> columInfos) {
            this.name = name;
            this.comment = comment;
            this.columInfos = columInfos;
        }

        public TableInfo(String name) {
            this.name = name;
        }
    }

    private static Connection getConnection(LinkInfo linkInfo) {
        try {
            DBType dbType = getDBType(linkInfo);
            Class.forName(dbType.getDriver());
            DriverManager.setLoginTimeout(3);
            return DriverManager.getConnection(dbType.getUrl(), linkInfo.getUser(), linkInfo.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public static List<ColumInfo> getTableColumInfo(LinkInfo linkInfo, String database, String table) {
        String sql = "";
        if (Objects.equals(DBTypeEnum.ORACLE.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select distinct cc.column_name as Field,tc.data_type as Type,cc.comments as \"Comment\" from user_col_comments cc,user_tab_columns tc " +
                    "where cc.column_name = tc.column_name and tc.table_name = '" + table + "'";
        } else if (Objects.equals(DBTypeEnum.MYSQL.getDbType(), linkInfo.getDatabaseType())) {
            sql = "show full columns from " + database + "." + table;

        } else if (Objects.equals(DBTypeEnum.MARIADB.getDbType(), linkInfo.getDatabaseType())) {
            sql = "show full columns from " + database + "." + table;
        } else if (Objects.equals(DBTypeEnum.SQLSERVER.getDbType(), linkInfo.getDatabaseType())) {
            sql = "SELECT " +
                    "a.name as Field, " +
                    "b.name as Type, " +
                    "isnull(convert(varchar(100), g.[value]),'') as Comment " +
                    "FROM syscolumns a " +
                    "left join systypes b on a.xusertype=b.xusertype " +
                    "inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' " +
                    "left join sys.extended_properties g on a.id=g.major_id and a.colid=g.minor_id " +
                    "left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0 " +
                    "where d.name='" + table + "' " +
                    "order by a.id,a.colorder";
            linkInfo.setDatabase(database);
        }

        BiConsumer<ResultSet, List<ColumInfo>> consumer = (rs, list) -> {

            try {
                int index = rs.getRow();
                String name = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");
                ColumInfo columInfo = new ColumInfo(name, type, comment, index);
                list.add(columInfo);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
        List<ColumInfo> columInfos = excuteSQL(sql, linkInfo, consumer);
        Set<ColumInfo> set = new HashSet<>(columInfos);
        columInfos = new ArrayList<>(set);
        columInfos.sort(Comparator.comparingInt(c -> c.index));
        return columInfos;
    }

    public static List<TableInfo> getTables(LinkInfo linkInfo, String database) {
        String sql = "";

        if (Objects.equals(DBTypeEnum.ORACLE.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select TABLE_NAME from user_tables";

        } else if (Objects.equals(DBTypeEnum.MYSQL.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select TABLE_NAME from INFORMATION_SCHEMA.TABLES Where table_schema = '" + database + "'";

        } else if (Objects.equals(DBTypeEnum.MARIADB.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select TABLE_NAME from INFORMATION_SCHEMA.TABLES Where table_schema = '" + database + "'";

        } else if (Objects.equals(DBTypeEnum.SQLSERVER.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select name from sysobjects where xtype='U'";
            linkInfo.setDatabase(database);
        }

        BiConsumer<ResultSet, List<TableInfo>> consumer = (rs, list) -> {
            try {
                list.add(new TableInfo(rs.getString(1)));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
        List<TableInfo> tableInfos = excuteSQL(sql, linkInfo, consumer);
        Set<TableInfo> set = new HashSet<>(tableInfos);
        tableInfos = new ArrayList<>(set);
        return tableInfos;
    }

    public static List<TableInfo> getTablesInfo(LinkInfo linkInfo, String database, List<String> tables) {
        String tableString = tables.toString().replace("[", "'").replace("]", "'").replace(", ", "', '");
        String sql = "";
        if (Objects.equals(DBTypeEnum.ORACLE.getDbType(), linkInfo.getDatabaseType())) {
            sql = "select a.TABLE_NAME as TABLE_NAME,b.COMMENTS as TABLE_COMMENT " +
                    "from user_tables a,user_tab_comments b " +
                    "WHERE a.TABLE_NAME = b.TABLE_NAME and a.TABLE_NAME IN([tables]) " +
                    "order by TABLE_NAME";
        } else if (Objects.equals(DBTypeEnum.MYSQL.getDbType(), linkInfo.getDatabaseType())) {
            sql = "Select table_name as TABLE_NAME,table_comment as TABLE_COMMENT " +
                    "from INFORMATION_SCHEMA.TABLES " +
                    "Where table_schema = '" + database + "' AND table_name in ([tables])";

        } else if (Objects.equals(DBTypeEnum.MARIADB.getDbType(), linkInfo.getDatabaseType())) {
            sql = "Select table_name as TABLE_NAME,table_comment as TABLE_COMMENT " +
                    "from INFORMATION_SCHEMA.TABLES " +
                    "Where table_schema = '" + database + "' AND table_name in ([tables])";
        } else if (Objects.equals(DBTypeEnum.SQLSERVER.getDbType(), linkInfo.getDatabaseType())) {

            sql = "SELECT " +
                    "d.name TABLE_NAME," +
                    "TABLE_COMMENT = case when a.colorder = 1 then isnull(convert(varchar(100), f.value),'') else '' end " +
                    "FROM syscolumns a " +
                    "inner join sysobjects d on a.id=d.id and d.xtype = 'U' and d.name<>'dtproperties' " +
                    "left join sys.extended_properties f on d.id = f.major_id and f.minor_id = 0 " +
                    "where d.name in ([tables])" +
                    "order by a.id,a.colorder";
            linkInfo.setDatabase(database);
        }
        sql = sql.replace("[tables]", tableString);

        BiConsumer<ResultSet, List<TableInfo>> consumer = (rs, list) -> {
            try {
                String tableName = rs.getString("TABLE_NAME");
                String tableComment = rs.getString("TABLE_COMMENT");

                List<ColumInfo> tableColumInfo = getTableColumInfo(linkInfo, database, tableName);
                list.add(new TableInfo(tableName, tableComment, tableColumInfo));
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
        List<TableInfo> tableInfos = excuteSQL(sql, linkInfo, consumer);
        Set<TableInfo> set = new HashSet<>(tableInfos);
        tableInfos = new ArrayList<>(set);
        return tableInfos;
    }
}
