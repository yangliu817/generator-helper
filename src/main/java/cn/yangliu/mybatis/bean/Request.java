package cn.yangliu.mybatis.bean;

import lombok.Data;

import java.util.List;

/**
 * The type Request.
 */
@Data
public class Request {

    private Long id;

    private Settings settings;

    private List<Database> databases;

    /**
     * The type Database.
     */
    @Data
    public static class Database{

        private String database;

        private List<Table> tables;

        /**
         * The type Table.
         */
        @Data
        public static class Table{
            private String table;
        }
    }

}
