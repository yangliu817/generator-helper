package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
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

    private JavaType defaultType = new JavaType();

    public EntityGenerator() {
        defaultType.setFullName("java.lang.String");
        defaultType.setShortName("String");
        defaultType.setNeedImport(false);
    }

    @Override
    public void generate(EntitySource source) {


        String code = template.t_entity;

        String packageName = source.getFullPackage();

        code = code.replace("[package]", "package " + packageName + ";");

        code = code.replace("[className]", source.getShortName());

        List<String> imports = new ArrayList<>();

        List<String> annotations = new ArrayList<>();

        List<String> fieldNames = new ArrayList<>();

        if (source.getUseBaseClass()) {
            String baseClassPackage = getClassPackage(source.getBaseClassFullName());
            if (!Objects.equals(source.getFullPackage(), baseClassPackage)) {
                imports.add(source.getBaseClassFullName());
            }
            code = code.replace("[extends]", " extends " + getClassShortName(source.getBaseClassFullName()));
        } else {
            code = code.replace("[extends]", "");
        }

        if (source.isMybatisPlus()) {
            annotations.add("TableName(\"" + source.getTableInfo().getName() + "\")");
            imports.add(ApplicationContant.config.getProperty("TableName"));
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

            String fieldCode = template.t_field.replace("[fieldType]", javaType.getShortName());

            if (javaType.isNeedImport()) {
                imports.add(javaType.getFullName());
            }

            String fieldName = CodeUtils.getFieldName(columnName);

            fieldNames.add(fieldName);
            fieldCode = fieldCode.replace("[fieldName]", fieldName);

            String comment = columInfo.getComment();

            boolean flag = false;
            if (Objects.equals(columnName, source.getPrimaryKeyName())) {
                if (source.isMybatisPlus()) {
                    imports.add(ApplicationContant.config.getProperty("TableId"));
                    fieldCode = fieldCode.replace("[annotations]", "@TableId");
                    flag = true;
                } else {
                    fieldCode = fieldCode.replace("[annotations]", "");
                }

            } else {
                fieldCode = fieldCode.replace("[annotations]", "");
            }

            if (StringUtils.isEmpty(comment)) {
                fieldCode = fieldCode.replace("[comment]", "");
            } else {
                String commentCode = template.t_comment.replace("[desp]", comment).trim();
                if (flag) {
                    commentCode = commentCode + "\n";
                }
                fieldCode = fieldCode.replace("[comment]", commentCode);
            }

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
            return javaFullTypeMap.get(source.getPrimaryKeyType());
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
                case "sqlServer":
                    ct = sqlServerColumnMap.get(columnType);
                    break;
                default:
                    break;
            }
            if (ct == null) {
                return defaultType;
            }

            JavaType javaType = column2javaTypeMap.get(ct);

            if (javaType == null) {
                return defaultType;
            }

            return javaType;
        }
        return javaFullTypeMap.get(javaTypeFullName);
    }

}
