package cn.yangliu.mybatis.bean;

import cn.yangliu.mybatis.controller.GeneratorController;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Request.
 */
@Data
public class Request {

    private Long linkId;

    private Settings settings;

    private List<Database> databases;

    private List<GeneratorController.SingleTableMapping> singleTableMappings = new ArrayList<>();

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
