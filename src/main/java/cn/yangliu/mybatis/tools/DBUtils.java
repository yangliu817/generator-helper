package cn.yangliu.mybatis.tools;

import cn.yangliu.mybatis.bean.LinkInfo;
import lombok.Data;
import org.apache.ibatis.io.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

            conn.close();
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
        switch (linkInfo.getDatabaseType()) {
            case "mysql":
                String url = "jdbc:mysql://" + linkInfo.getHost() + ":" + linkInfo.getPort();
                String driver = "com.mysql.jdbc.Driver";
                return new DBType(url, driver);
            default:
                throw new NullPointerException("不支持的数据库类型");
        }
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

    public static List<String> getDatabases(LinkInfo linkInfo) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> databases = new ArrayList<>();
        try {
            DBType dbType = getDBType(linkInfo);
            Class.forName(dbType.getDriver());
            connection = DriverManager.getConnection(dbType.getUrl(), linkInfo.getUser(), linkInfo.getPassword());

            ps = connection.prepareStatement("show databases;");

            rs = ps.executeQuery();

            while (rs.next()) {
                databases.add(rs.getString(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, ps, connection);
        }
        return databases;
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
    public static class ColumInfo {

        private String name;

        private String type;

        private String comment;

        public ColumInfo(String name, String type, String comment) {
            this.name = name;
            this.type = type;
            this.comment = comment;
        }
    }

    @Data
    public static class TableInfo {

        private String name;

        private String comment;

        private List<ColumInfo> columInfos;

        public TableInfo(String name, String comment, List<ColumInfo> columInfos) {
            this.name = name;
            this.comment = comment;
            this.columInfos = columInfos;
        }
    }

    public static List<ColumInfo> getTableColumInfo(LinkInfo linkInfo, String database, String table) {
        String sql = "show full columns from " + database + "." + table + ";";
        List<ColumInfo> columInfoList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DBType dbType = getDBType(linkInfo);
            Class.forName(dbType.getDriver());
            connection = DriverManager.getConnection(dbType.getUrl(), linkInfo.getUser(), linkInfo.getPassword());


            ps = connection.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                String name = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");

                ColumInfo columInfo = new ColumInfo(name, type, comment);
                columInfoList.add(columInfo);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, ps, connection);
        }
        return columInfoList;
    }


    public static List<String> getTables(LinkInfo linkInfo, String database) {
        List<String> tables = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DBType dbType = getDBType(linkInfo);
            Class.forName(dbType.getDriver());
            connection = DriverManager.getConnection(dbType.getUrl(), linkInfo.getUser(), linkInfo.getPassword());

            String sql = "select table_name from INFORMATION_SCHEMA.TABLES Where table_schema = ?;";
            String params = "";

            for (int i = 0; i < tables.size(); i++) {
                params += "'" + tables.get(i) + "'";
                if ((i + 1) != tables.size()) {
                    params += ",";
                }
            }
            sql = String.format(sql, params);

            ps = connection.prepareStatement(sql);
            ps.setString(1, database);
            rs = ps.executeQuery();

            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, ps, connection);
        }
        return tables;
    }

    public static List<TableInfo> getTablesInfo(LinkInfo linkInfo, String database, List<String> tables) {

        if (tables.size() == 0) {
            throw new NullPointerException();
        }

        List<TableInfo> tableInfos = new ArrayList<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            DBType dbType = getDBType(linkInfo);
            Class.forName(dbType.getDriver());
            connection = DriverManager.getConnection(dbType.getUrl(), linkInfo.getUser(), linkInfo.getPassword());

            String sql = "Select table_name,table_comment  from INFORMATION_SCHEMA.TABLES Where table_schema = ? AND table_name in ([tables]);";

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < tables.size(); i++) {
                String table = tables.get(i);
                sb.append("'").append(table).append("'");
                if ((i + 1) != tables.size()) {
                    sb.append(",");
                }
            }

            sql = sql.replace("[tables]", sb.toString());

            ps = connection.prepareStatement(sql);
            ps.setString(1, database);
            rs = ps.executeQuery();

            while (rs.next()) {
                String tableName = rs.getString("table_name");
                String tableComment = rs.getString("table_comment");

                List<ColumInfo> tableColumInfo = getTableColumInfo(linkInfo, database, tableName);
                tableInfos.add(new TableInfo(tableName, tableComment, tableColumInfo));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            close(rs, ps, connection);
        }
        return tableInfos;
    }

}
