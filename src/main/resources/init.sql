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
INSERT INTO "t_column_type" VALUES (25, 'VARCHAR2', 'oracle');
INSERT INTO "t_column_type" VALUES (26, 'UROWID', 'oracle');
INSERT INTO "t_column_type" VALUES (27, 'TIMESTAMP WITH TIME ZONE', 'oracle');
INSERT INTO "t_column_type" VALUES (28, 'TIMESTAMP WITH LOCAL TIME ZONE', 'oracle');
INSERT INTO "t_column_type" VALUES (29, 'TIMESTAMP', 'oracle');
INSERT INTO "t_column_type" VALUES (30, 'NUMBER', 'oracle');
INSERT INTO "t_column_type" VALUES (31, 'LONG RAW', 'oracle');
INSERT INTO "t_column_type" VALUES (32, 'LONG', 'oracle');
INSERT INTO "t_column_type" VALUES (33, 'INTERVAL YEAR TO MONTH', 'oracle');
INSERT INTO "t_column_type" VALUES (34, 'INTERVAL DAY TO SECOND', 'oracle');
INSERT INTO "t_column_type" VALUES (35, 'FLOAT', 'oracle');
INSERT INTO "t_column_type" VALUES (36, 'DATE', 'oracle');
INSERT INTO "t_column_type" VALUES (37, 'CHARACTER', 'oracle');
INSERT INTO "t_column_type" VALUES (38, 'CHAR', 'oracle');
INSERT INTO "t_column_type" VALUES (39, 'BINARY_FLOAT', 'oracle');
INSERT INTO "t_column_type" VALUES (40, 'BINARY_DOUBLE', 'oracle');
INSERT INTO "t_column_type" VALUES (41, 'int', 'mariadb');
INSERT INTO "t_column_type" VALUES (42, 'tinyint', 'mariadb');
INSERT INTO "t_column_type" VALUES (43, 'smallint', 'mariadb');
INSERT INTO "t_column_type" VALUES (44, 'mediumint', 'mariadb');
INSERT INTO "t_column_type" VALUES (45, 'integer', 'mariadb');
INSERT INTO "t_column_type" VALUES (46, 'bigint', 'mariadb');
INSERT INTO "t_column_type" VALUES (47, 'float', 'mariadb');
INSERT INTO "t_column_type" VALUES (48, 'double', 'mariadb');
INSERT INTO "t_column_type" VALUES (49, 'char', 'mariadb');
INSERT INTO "t_column_type" VALUES (50, 'varchar', 'mariadb');
INSERT INTO "t_column_type" VALUES (51, 'tinyblob', 'mariadb');
INSERT INTO "t_column_type" VALUES (52, 'tinytext', 'mariadb');
INSERT INTO "t_column_type" VALUES (53, 'blob', 'mariadb');
INSERT INTO "t_column_type" VALUES (54, 'text', 'mariadb');
INSERT INTO "t_column_type" VALUES (55, 'mediumblob', 'mariadb');
INSERT INTO "t_column_type" VALUES (56, 'mediumtext', 'mariadb');
INSERT INTO "t_column_type" VALUES (57, 'longblob', 'mariadb');
INSERT INTO "t_column_type" VALUES (58, 'longtext', 'mariadb');
INSERT INTO "t_column_type" VALUES (59, 'date', 'mariadb');
INSERT INTO "t_column_type" VALUES (60, 'time', 'mariadb');
INSERT INTO "t_column_type" VALUES (61, 'year', 'mariadb');
INSERT INTO "t_column_type" VALUES (62, 'datetime', 'mariadb');
INSERT INTO "t_column_type" VALUES (63, 'timestamp', 'mariadb');
INSERT INTO "t_column_type" VALUES (64, 'decimal', 'mariadb');
INSERT INTO "t_column_type" VALUES (65, 'int', 'sqlserver');
INSERT INTO "t_column_type" VALUES (66, 'bigint', 'sqlserver');
INSERT INTO "t_column_type" VALUES (67, 'binary', 'sqlserver');
INSERT INTO "t_column_type" VALUES (68, 'bit', 'sqlserver');
INSERT INTO "t_column_type" VALUES (69, 'char', 'sqlserver');
INSERT INTO "t_column_type" VALUES (70, 'date', 'sqlserver');
INSERT INTO "t_column_type" VALUES (71, 'datetime', 'sqlserver');
INSERT INTO "t_column_type" VALUES (72, 'datetime2', 'sqlserver');
INSERT INTO "t_column_type" VALUES (73, 'datetimeoffset', 'sqlserver');
INSERT INTO "t_column_type" VALUES (74, 'decimal', 'sqlserver');
INSERT INTO "t_column_type" VALUES (75, 'real', 'sqlserver');
INSERT INTO "t_column_type" VALUES (76, 'image', 'sqlserver');
INSERT INTO "t_column_type" VALUES (77, 'money', 'sqlserver');
INSERT INTO "t_column_type" VALUES (78, 'nchar', 'sqlserver');
INSERT INTO "t_column_type" VALUES (79, 'ntext', 'sqlserver');
INSERT INTO "t_column_type" VALUES (80, 'numeric', 'sqlserver');
INSERT INTO "t_column_type" VALUES (81, 'nvarchar', 'sqlserver');
INSERT INTO "t_column_type" VALUES (82, 'smalldatetime', 'sqlserver');
INSERT INTO "t_column_type" VALUES (83, 'smallint', 'sqlserver');
INSERT INTO "t_column_type" VALUES (84, 'smallmoney', 'sqlserver');
INSERT INTO "t_column_type" VALUES (85, 'sql_variant', 'sqlserver');
INSERT INTO "t_column_type" VALUES (86, 'sysname', 'sqlserver');
INSERT INTO "t_column_type" VALUES (87, 'text', 'sqlserver');
INSERT INTO "t_column_type" VALUES (88, 'time', 'sqlserver');
INSERT INTO "t_column_type" VALUES (89, 'timestamp', 'sqlserver');
INSERT INTO "t_column_type" VALUES (90, 'tinyint', 'sqlserver');
INSERT INTO "t_column_type" VALUES (91, 'varbinary', 'sqlserver');
INSERT INTO "t_column_type" VALUES (92, 'xml', 'sqlserver');
INSERT INTO "t_column_type" VALUES (93, 'varchar', 'sqlserver');
INSERT INTO "t_column_type" VALUES (94, 'uniqueidentifier', 'sqlserver');

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
INSERT INTO "t_java_type" VALUES (18, 'Character', 'java.lang.Character', 0);
INSERT INTO "t_java_type" VALUES (19, 'Clob', 'java.sql.Clob', 1);
INSERT INTO "t_java_type" VALUES (20, 'DateTimeOffset', 'microsoft.sql.DateTimeOffset', 1);
INSERT INTO "t_java_type" VALUES (21, 'byte[]', 'byte[]', '0');

CREATE TABLE "t_link_info" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "name" TEXT,
  "user" TEXT,
  "password" TEXT,
  "port" TEXT,
  "host" TEXT,
  "database_type" TEXT,
  "service" TEXT
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
INSERT INTO "t_mapping_setting" VALUES (5, 10, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (6, 11, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (7, 12, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (8, 13, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (9, 14, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (10, 15, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (11, 16, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (12, 17, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (13, 18, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (14, 19, 12, 0);
INSERT INTO "t_mapping_setting" VALUES (15, 20, 14, 0);
INSERT INTO "t_mapping_setting" VALUES (16, 22, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (17, 23, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (18, 24, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (19, 25, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (20, 26, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (21, 27, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (22, 28, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (23, 29, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (24, 30, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (25, 31, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (26, 32, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (27, 33, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (28, 34, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (29, 35, 16, 0);
INSERT INTO "t_mapping_setting" VALUES (30, 36, 12, 0);
INSERT INTO "t_mapping_setting" VALUES (31, 37, 18, 0);
INSERT INTO "t_mapping_setting" VALUES (32, 38, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (33, 39, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (34, 40, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (35, 28, 17, 0);
INSERT INTO "t_mapping_setting" VALUES (36, 30, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (37, 41, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (38, 42, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (39, 43, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (40, 44, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (41, 50, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (42, 51, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (43, 52, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (44, 53, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (45, 54, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (46, 55, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (47, 56, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (48, 57, 9, 0);
INSERT INTO "t_mapping_setting" VALUES (49, 58, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (50, 59, 12, 0);
INSERT INTO "t_mapping_setting" VALUES (51, 60, 14, 0);
INSERT INTO "t_mapping_setting" VALUES (52, 62, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (53, 63, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (54, 64, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (55, 65, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (56, 66, 2, 0);
INSERT INTO "t_mapping_setting" VALUES (57, 67, 21, 0);
INSERT INTO "t_mapping_setting" VALUES (58, 68, 4, 0);
INSERT INTO "t_mapping_setting" VALUES (59, 69, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (60, 70, 12, 0);
INSERT INTO "t_mapping_setting" VALUES (61, 71, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (62, 72, 13, 0);
INSERT INTO "t_mapping_setting" VALUES (63, 73, 20, 0);
INSERT INTO "t_mapping_setting" VALUES (64, 74, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (65, 75, 16, 0);
INSERT INTO "t_mapping_setting" VALUES (66, 76, 21, 0);
INSERT INTO "t_mapping_setting" VALUES (67, 77, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (68, 78, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (69, 79, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (70, 80, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (71, 81, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (72, 82, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (73, 83, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (74, 84, 10, 0);
INSERT INTO "t_mapping_setting" VALUES (75, 85, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (76, 86, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (77, 87, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (78, 88, 14, 0);
INSERT INTO "t_mapping_setting" VALUES (79, 89, 8, 0);
INSERT INTO "t_mapping_setting" VALUES (80, 90, 15, 0);
INSERT INTO "t_mapping_setting" VALUES (81, 91, 21, 0);
INSERT INTO "t_mapping_setting" VALUES (82, 92, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (83, 93, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (84, 94, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (85, 5, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (86, 6, 2, 0);
INSERT INTO "t_mapping_setting" VALUES (87, 7, 16, 0);
INSERT INTO "t_mapping_setting" VALUES (88, 8, 17, 0);
INSERT INTO "t_mapping_setting" VALUES (89, 9, 18, 0);
INSERT INTO "t_mapping_setting" VALUES (90, 21, 1, 0);
INSERT INTO "t_mapping_setting" VALUES (91, 45, 3, 0);
INSERT INTO "t_mapping_setting" VALUES (92, 46, 2, 0);
INSERT INTO "t_mapping_setting" VALUES (93, 47, 16, 0);
INSERT INTO "t_mapping_setting" VALUES (94, 48, 17, 0);
INSERT INTO "t_mapping_setting" VALUES (95, 49, 18, 0);
INSERT INTO "t_mapping_setting" VALUES (96, 61, 1, 0);

CREATE TABLE "t_project_setting" (
  "id" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  "setting_id" INTEGER,
  "project_package" TEXT,
  "code_path" TEXT,
  "use_lombok" TEXT,
  "create_service" TEXT,
  "create_controller" TEXT,
  "table_prefix" TEXT,
  "mybatis_type" TEXT,
  "author" TEXT,
  "contact" TEXT,
  "primary_key_name" TEXT
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