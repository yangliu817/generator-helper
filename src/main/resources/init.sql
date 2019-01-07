CREATE TABLE "t_column_type" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "column_type" TEXT,
  "db_type" TEXT
);

INSERT INTO "t_column_type" VALUES (1, 'int', 'mysql');
INSERT INTO "t_column_type" VALUES (2, 'tinyint', 'mysql');
INSERT INTO "t_column_type" VALUES (3, 'smallint', 'mysql');
INSERT INTO "t_column_type" VALUES (4, 'mediumint', 'mysql');
INSERT INTO "t_column_type" VALUES (5, 'integer', 'mysql');
INSERT INTO "t_column_type" VALUES (6, 'bigint', 'mysql');
INSERT INTO "t_column_type" VALUES (7, 'float', 'mysql');
INSERT INTO "t_column_type" VALUES (8, 'double', 'mysql');
INSERT INTO "t_column_type" VALUES (9, 'char', 'mysql');
INSERT INTO "t_column_type" VALUES (10, 'varchar', 'mysql');
INSERT INTO "t_column_type" VALUES (11, 'tinyblob', 'mysql');
INSERT INTO "t_column_type" VALUES (12, 'tinytext', 'mysql');
INSERT INTO "t_column_type" VALUES (13, 'blob', 'mysql');
INSERT INTO "t_column_type" VALUES (14, 'text', 'mysql');
INSERT INTO "t_column_type" VALUES (15, 'mediumblob', 'mysql');
INSERT INTO "t_column_type" VALUES (16, 'mediumtext', 'mysql');
INSERT INTO "t_column_type" VALUES (17, 'longblob', 'mysql');
INSERT INTO "t_column_type" VALUES (18, 'longtext', 'mysql');
INSERT INTO "t_column_type" VALUES (19, 'date', 'mysql');
INSERT INTO "t_column_type" VALUES (20, 'time', 'mysql');
INSERT INTO "t_column_type" VALUES (21, 'year', 'mysql');
INSERT INTO "t_column_type" VALUES (22, 'datetime', 'mysql');
INSERT INTO "t_column_type" VALUES (23, 'timestamp', 'mysql');
INSERT INTO "t_column_type" VALUES (24, 'decimal', 'mysql');

CREATE TABLE "t_controller_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "setting_id" INTEGER,
  "controller_package" TEXT,
  "method_return_type_full_name" TEXT,
  "return_type_static_method" TEXT,
  "use_restful" TEXT
);


CREATE TABLE "t_entity_setting" (
  "id" integer NOT NULL PRIMARY KEY AUTOINCREMENT,
  "entity_package" TEXT,
  "base_class_full_name" TEXT,
  "equal_and_hash" text,
  "to_string" TEXT,
  "no_arg_constructor" TEXT,
  "chain" TEXT,
  "use_base_class" TEXT,
  "setting_id" INTEGER,
  "primary_key_name" TEXT,
  "exclude_columns" TEXT,
  "primary_key_type" TEXT
);

CREATE TABLE "t_java_type" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "short_name" TEXT,
  "full_name" TEXT,
  "need_import" TEXT
);


INSERT INTO "t_java_type" VALUES (1, 'String', 'java.lang.String', 0);
INSERT INTO "t_java_type" VALUES (2, 'Long', 'java.lang.Long', 0);
INSERT INTO "t_java_type" VALUES (3, 'Integer', 'java.lang.Integer', 0);
INSERT INTO "t_java_type" VALUES (4, 'Boolean', 'java.lang.Boolean', 0);
INSERT INTO "t_java_type" VALUES (5, 'Byte', 'java.lang.Byte', 0);
INSERT INTO "t_java_type" VALUES (6, 'Date', 'java.util.Date', 1);
INSERT INTO "t_java_type" VALUES (7, 'Date', 'java.sql.Date', 1);
INSERT INTO "t_java_type" VALUES (8, 'Timestamp', 'java.sql.Timestamp', 1);
INSERT INTO "t_java_type" VALUES (9, 'Blob', 'java.sql.Blob', 1);
INSERT INTO "t_java_type" VALUES (10, 'BigDecimal', 'java.math.BigDecimal', 1);
INSERT INTO "t_java_type" VALUES (11, 'BigInteger', 'java.math.BigInteger', 1);
INSERT INTO "t_java_type" VALUES (12, 'LocalDate', 'java.time.LocalDate', 1);
INSERT INTO "t_java_type" VALUES (13, 'LocalDateTime', 'java.time.LocalDateTime', 1);
INSERT INTO "t_java_type" VALUES (14, 'LocalTime', 'java.time.LocalTime', 1);
INSERT INTO "t_java_type" VALUES (15, 'Short', 'java.lang.Short', 0);
INSERT INTO "t_java_type" VALUES (16, 'Float', 'java.lang.Float', 0);
INSERT INTO "t_java_type" VALUES (17, 'Double', 'java.lang.Double', 0);


CREATE TABLE "t_link_info" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "name" TEXT,
  "user" TEXT,
  "password" TEXT,
  "port" TEXT,
  "host" TEXT,
  "database_type" TEXT
);

CREATE TABLE "t_mapper_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "setting_id" INTEGER,
  "mapper_package" TEXT,
  "mapper_sufix" TEXT,
  "extend_base_mapper" TEXT,
  "use_mapper_anonntation" TEXT
);

CREATE TABLE "t_mapping_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "column_type_id" TEXT,
  "java_type_id" INTEGER,
  "setting_id" INTEGER
);


INSERT INTO "t_mapping_setting" VALUES (1, 1, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (2, 2, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (3, 3, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (4, 4, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (5, 5, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (6, 6, 2, 0);
INSERT INTO "t_mapping_setting" VALUES (7, 7, 16, 0);
INSERT INTO "t_mapping_setting" VALUES (8, 8, 17, 0);
INSERT INTO "t_mapping_setting" VALUES (9, 10, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (10, 11, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (11, 12, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (12, 13, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (13, 14, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (14, 15, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (15, 16, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (16, 17, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (17, 18, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (18, 19, 12, 0);
INSERT INTO "t_mapping_setting" VALUES (19, 20, 14, 0);
INSERT INTO "t_mapping_setting" VALUES (20, 24, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (21, 22, 13, 0);

CREATE TABLE "t_project_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "setting_id" INTEGER,
  "project_package" TEXT,
  "code_path" TEXT,
  "use_lombok" TEXT,
  "create_service" TEXT,
  "create_controller" TEXT,
  "table_prefix" TEXT,
  "mybatis_type" TEXT
);

CREATE TABLE "t_service_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "setting_id" INTEGER,
  "service_package" TEXT,
  "create_interface" TEXT,
  "use_base_service" TEXT,
  "use_transactional" TEXT
);

CREATE TABLE "t_settings_info" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "link_id" INTEGER,
  "name" TEXT
);


