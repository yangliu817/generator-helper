package cn.yangliu.mybatis.bean;

import lombok.Data;

import java.util.List;

@Data
public class Request {

    private Long id;

    private Settings settings;

    private List<Database> databases;

    @Data
    public static class Database{

        private String database;

        private List<Table> tables;

        @Data
        public static class Table{
            private String table;
        }
    }

}
