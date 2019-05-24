package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.enums.StrategyEnum;
import cn.yangliu.mybatis.source.EntitySource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class EntityGenerator extends AbstractGenerator<EntitySource> {

    @Override
    public void generate(EntitySource source) {
        String code = template.t_entity;

        code = generateCopyRight(code, source);

        code = generateComments(code, source);

        code = generatePackage(source, code);

        code = code.replace("[className]", source.getShortName());

        List<String> imports = new ArrayList<>();

        List<String> annotations = new ArrayList<>();

        List<String> fieldNames = new ArrayList<>();

        String extendsCode = "";
        if (source.getUseBaseClass()) {
            String baseClassPackage = getClassPackage(source.getBaseClassFullName());
            if (!Objects.equals(source.getFullPackage(), baseClassPackage)) {
                imports.add(source.getBaseClassFullName());
            }
            extendsCode = " extends " + getClassShortName(source.getBaseClassFullName());
        }
        code = code.replace("[extends]", extendsCode);
        if (source.getUseSwagger()) {
            imports.add(ApplicationContant.config.getProperty("ApiModel"));
            annotations.add("ApiModel");
        }
        if (Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)) {
            annotations.add("TableName(\"" + source.getTableInfo().getName() + "\")");
            imports.add(ApplicationContant.config.getProperty("TableName"));
        } else if (Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
            annotations.add("Table(name = \"" + source.getTableInfo().getName() + "\")");
            imports.add(ApplicationContant.config.getProperty("Table"));

            annotations.add("Entity");
            imports.add(ApplicationContant.config.getProperty("Entity"));
        }

        StringBuilder setterAndGetterCode = new StringBuilder();

        String fieldsCode = generateFieldCodes(source, imports, annotations, fieldNames, setterAndGetterCode);
        code = code.replace("[fields]", fieldsCode);

        if (source.getUseLombok()) {

            if (source.getToString()) {
                annotations.add("ToString");
                imports.add(ApplicationContant.config.getProperty("ToString"));
            }

            if (source.getChain()) {
                annotations.add("Accessors(chain = true)");
                imports.add(ApplicationContant.config.getProperty("Accessors"));
            }

            if (source.getEqualAndHash()) {
                annotations.add("EqualsAndHashCode(callSuper = false)");
                imports.add(ApplicationContant.config.getProperty("EqualsAndHashCode"));
            }

            if (source.getNoArgConstructor()) {
                annotations.add("NoArgsConstructor");
                imports.add(ApplicationContant.config.getProperty("NoArgsConstructor"));

                code = code.replace("[constructor]", "");

            }

            annotations.add("Getter");
            imports.add(ApplicationContant.config.getProperty("Getter"));
            annotations.add("Setter");
            imports.add(ApplicationContant.config.getProperty("Setter"));

            code = code.replace("[setter-getter]", "");

        } else {
            //生成代码形式的get set
            code = code.replace("[setter-getter]", setterAndGetterCode.toString());
        }

        code = generateConstructor(source, code);

        code = generateEqualsAndHashCode(source, code, fieldNames, imports);

        code = generateToString(source, code, fieldNames);

        code = generateImports(code, imports);

        code = generateAnnotations(code, annotations);

        String comment = source.getTableInfo().getComment();

        if (StringUtils.isEmpty(comment)) {
            comment = "";
        }

        code = code.replace("[comment]", comment);

        FileUtils.output(code, source.getFilepath(), source.getFilename());

    }


    private String generateToString(EntitySource entitySource, String sourceCode, List<String> fieldNames) {
        String toString = "";
        if (!entitySource.getUseLombok() && entitySource.getToString()) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < fieldNames.size(); i++) {
                String ifCode = template.t_if;
                if ((i + 1) == fieldNames.size()) {
                    ifCode = template.t_if_end;
                }
                ifCode = ifCode.replace("[fieldName]", fieldNames.get(i));
                sb.append(ifCode);
            }

            toString = template.t_toString.replace("[className]", entitySource.getShortName());

            toString = toString.replace("[if]", sb.toString());
        }

        return sourceCode.replace("[toString]", toString);

    }


    private String generateEqualsAndHashCode(EntitySource entitySource, String sourceCode, List<String> fieldNames, List<String> imports) {
        String equalsAndHashCode = "";
        if (entitySource.getEqualAndHash() && !entitySource.getUseLombok()) {
            imports.add(ApplicationContant.config.getProperty("Objects"));
            equalsAndHashCode = generateEqualsCode(entitySource, fieldNames) + generateHashCode(fieldNames);
        }
        return sourceCode.replace("[equals-hash]", equalsAndHashCode);
    }

    private String generateEqualsCode(EntitySource entitySource, List<String> fieldNames) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String t = template.t_equals;

            if ((i + 1) == fieldNames.size()) {
                t = t.replace("[fieldName]", fieldNames.get(i)).replace(" &&", ";");
            } else {
                t = t.replace("[fieldName]", fieldNames.get(i));
            }
            sb.append(t);
        }

        String equalsCode = template.t_equal_method.replace("[className]", entitySource.getShortName())
                .replace("[className-l]", CodeUtils.firstChar2Lowercase(entitySource.getShortName()));
        equalsCode = equalsCode.replace("[equals]", sb.toString().trim());

        return equalsCode + "\n";
    }

    private String generateHashCode(List<String> fieldNames) {
        String fields = fieldNames.toString().replace("[", "").replace("]", "");
        return template.t_hash.replace("[fields]", fields) + "\n";
    }

    private String generateConstructor(EntitySource source, String code) {
        String constructorCode = "";
        if (source.getNoArgConstructor() && !source.getUseLombok()) {
            constructorCode = template.t_constructor.replace("[className]", source.getShortName());
        }
        return code.replace("[constructor]", constructorCode);
    }

    private void generateGetterCode(String fieldType, String fieldName, StringBuilder setterAndGetterCode) {
        String code = template.t_getter.replace("[returnType]", fieldType)
                .replace("[fieldName-u]", CodeUtils.firstChar2Uppercase(fieldName))
                .replace("[fieldName]", fieldName);
        setterAndGetterCode.append(code).append("\n");
    }

    private void generateSetterCode(boolean chain, String fieldType, String className, String fieldName, StringBuilder setterAndGetterCode) {

        if (chain) {
            String code = template.t_setter_chain.replace("[className]", className)
                    .replace("[fieldName-u]", CodeUtils.firstChar2Uppercase(fieldName))
                    .replace("[fieldType]", fieldType)
                    .replace("[fieldName]", fieldName);
            setterAndGetterCode.append(code).append("\n");
            return;
        }
        String code = template.t_setter.replace("[fieldName-u]", CodeUtils.firstChar2Uppercase(fieldName))
                .replace("[fieldType]", fieldType)
                .replace("[fieldName]", fieldName);
        setterAndGetterCode.append(code).append("\n");

    }


    private String generateFieldCodes(EntitySource source, List<String> imports, List<String> annotations, List<String> fieldNames, StringBuilder setterAndGetterCode) {

        StringBuilder sb = new StringBuilder();

        List<DBUtils.ColumInfo> columInfos = source.getTableInfo().getColumInfos();

        for (DBUtils.ColumInfo columInfo : columInfos) {

            String columnName = columInfo.getName();
            if (source.getExcludeColumns().contains(columnName)) {
                continue;
            }

            String dbColumnType = CodeUtils.getColumnType(columInfo.getType());

            JavaType javaType = getJavaType(source, columnName, dbColumnType);

            source.getColumnMapping().put(columnName, javaType);
            String fieldCode = template.t_field.replace("[fieldType]", javaType.getShortName());


            if (javaType.isNeedImport()) {
                imports.add(javaType.getFullName());
            }

            String fieldName = CodeUtils.getFieldName(columnName);

            fieldNames.add(fieldName);
            fieldCode = fieldCode.replace("[fieldName]", fieldName);

            String comment = columInfo.getComment();

            String commentCode = "";
            if (StringUtils.isNotEmpty(comment)) {
                commentCode = template.t_comment.replace("[desp]", comment).trim();
            }


            String annotationCode = "";
            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)) {
                imports.add(ApplicationContant.config.getProperty("TableId"));
                annotationCode = "@TableId";
            }
            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                    && StringUtils.isNotEmpty(comment)) {
                annotationCode = "\n    @TableId";
            }
            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                    && !Objects.equals(source.getStrategy(), "0")) {
                annotationCode = "\n    @TableId";
            }
            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
                imports.add(ApplicationContant.config.getProperty("Id"));
                imports.add(ApplicationContant.config.getProperty("GeneratedValue"));
                annotationCode = "@Id\n    @GeneratedValue";
            }

            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)
                    && StringUtils.isNotEmpty(comment)) {
                annotationCode = "\n    @Id\n    @GeneratedValue";
            }

            StrategyEnum strategy = null;
            if (!Objects.equals(source.getStrategy(), "0")
                    && !Objects.equals(source.getOrmType(), OrmTypeEnum.Mybatis)) {
                strategy = StrategyEnum.getEnumByStrategy(source.getStrategy());
            }
            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.MybatisPlus)
                    && Objects.nonNull(strategy)) {
                imports.add(ApplicationContant.config.getProperty("IdType"));
                annotationCode = annotationCode + "(type = IdType." + strategy.getName() + ")";
            }

            if (Objects.equals(columnName, source.getPrimaryKeyName())
                    && Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)
                    && Objects.nonNull(strategy)) {
                imports.add(ApplicationContant.config.getProperty("GenerationType"));
                annotationCode = annotationCode + "(strategy = GenerationType." + strategy.getName() + ")";
            }

            if (!Objects.equals(columnName, source.getPrimaryKeyName()) && Objects.equals(source.getOrmType(), OrmTypeEnum.JPA)) {
                annotationCode = "\n    @Column(name = \"" + columnName + "\")";
                imports.add(ApplicationContant.config.getProperty("Column"));
            }

            if (source.getUseSwagger() && StringUtils.isNotEmpty(comment)) {
                annotationCode = annotationCode + "\n    @ApiModelProperty(value = \"" + comment + "\")";
                imports.add(ApplicationContant.config.getProperty("ApiModelProperty"));
            }

            if (source.getUseSwagger() && StringUtils.isEmpty(comment)) {
                annotationCode = annotationCode + "\n    @ApiModelProperty()";
                imports.add(ApplicationContant.config.getProperty("ApiModelProperty"));
            }

            if (StringUtils.isNotEmpty(comment)) {
                commentCode = "\n    " + commentCode;
            }
            fieldCode = fieldCode.replace("[comment]", commentCode);
            fieldCode = fieldCode.replace("[annotations]", annotationCode);


            sb.append(fieldCode);

            if (!source.getUseLombok()) {
                generateSetterCode(source.getChain(), javaType.getShortName(), source.getShortName(), fieldName, setterAndGetterCode);
                generateGetterCode(javaType.getShortName(), fieldName, setterAndGetterCode);
            }
        }
        return sb.toString();
    }

    private JavaType getJavaType(EntitySource source, String columnName, String columnType) {

        //判断是不是主键
        if (Objects.equals(source.getPrimaryKeyName(), columnName) && javaFullTypeMap.containsKey(source.getPrimaryKeyType())) {
            return javaFullTypeMap.getOrDefault(source.getPrimaryKeyType(), JavaType.DEFAULT);
        }

        if (source.isSingleTable()) {
            return source.getSingleTableMapping().getOrDefault(columnName, JavaType.DEFAULT);
        }
        String javaTypeFullName = source.getMapping().get(columnType);

        if (StringUtils.isEmpty(javaTypeFullName)) {
            ColumnType ct = null;
            switch (source.getDbType()) {
                case "mysql":
                    ct = mysqlColumnMap.get(columnType);
                    break;
                case "mariadb":
                    ct = mariadbColumnMap.get(columnType);
                    break;
                case "oracle":
                    ct = oracleColumnMap.get(columnType);
                    break;
                case "sqlserver":
                    ct = sqlserverColumnMap.get(columnType);
                    break;
                default:
                    break;
            }
            if (ct == null) {
                return JavaType.DEFAULT;
            }

            return column2javaTypeMap.getOrDefault(ct, JavaType.DEFAULT);
        }
        return javaFullTypeMap.getOrDefault(javaTypeFullName, JavaType.DEFAULT);
    }

}
