package cn.yangliu.mybatis.gennerator;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.source.EntitySource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.*;

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

        annotations.add("TableName(\"" + source.getTableInfo().getName() + "\")");
        imports.add(ApplicationContant.config.getProperty("TableName"));

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

        code = generateEqualsAndHashCode(source, code, fieldNames);

        code = generateToString(source, code, fieldNames);

        code = generateImports(code, imports);

        code = generateAnnontations(code, annotations);

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


    private String generateEqualsAndHashCode(EntitySource entitySource, String sourceCode, List<String> fieldNames) {
        String equalsAndHashCode = "";
        if (entitySource.getEqualAndHash() && !entitySource.getUseLombok()) {
            equalsAndHashCode = generateEqualsCode(entitySource, fieldNames) + generateHashCode(fieldNames);
        }
        return sourceCode.replace("[equals-hash]", equalsAndHashCode);
    }

    private String generateEqualsCode(EntitySource entitySource, List<String> fieldNames) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String t;
            if ((i + 1) == fieldNames.size()) {
                t = template.t_equals.replace("[fieldName]", fieldNames.get(i)).replace("&&", ";");
            } else {
                t = template.t_equals.replace("[fieldName]", fieldNames.get(i));
            }
            sb.append(t);
        }

        String equalsCode = template.t_equal_method.replace("[className]", entitySource.getShortName())
                .replace("[className-l]", CodeUtils.firstChar2Lowercase(entitySource.getShortName()));
        equalsCode = equalsCode.replace("[equals]", sb.toString());

        return equalsCode;
    }

    private String generateHashCode(List<String> fieldNames) {
        String fields = fieldNames.toString().replace("[", "").replace("]", "");
        return template.t_hash.replace("[fields]", fields);
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
        setterAndGetterCode.append(code);
    }

    private void generateSetterCode(boolean chain, String fieldType, String className, String fieldName, StringBuilder setterAndGetterCode) {

        String code;
        if (chain) {
            code = template.t_setter_chain.replace("[className]", className)
                    .replace("[fieldName-u]", CodeUtils.firstChar2Uppercase(fieldName))
                    .replace("[fieldType]", fieldType)
                    .replace("[fieldName]", fieldName)
                    .replace("[fieldName]", fieldName)
                    .replace("[fieldName]", fieldName);
        } else {
            code = template.t_setter.replace("[fieldName-u]", CodeUtils.firstChar2Uppercase(fieldName))
                    .replace("[fieldType]", fieldType)
                    .replace("[fieldName]", fieldName)
                    .replace("[fieldName]", fieldName);
        }
        setterAndGetterCode.append(code);
    }


    private String generateFieldCodes(EntitySource source, List<String> imports, List<String> annotations, List<String> fieldNames, StringBuilder setterAndGetterCode) {
        Map<Long, Long> mapping = new HashMap<>();

        source.getMapping().forEach(m -> mapping.put(m.getColumnTypeId(), m.getJavaTypeId()));

        StringBuilder sb = new StringBuilder();

        List<DBUtils.ColumInfo> columInfos = source.getTableInfo().getColumInfos();

        for (DBUtils.ColumInfo columInfo : columInfos) {

            String columnName = columInfo.getName();
            if (source.getExcludeColumns().contains(columnName)) {
                continue;
            }

            String dbColumnType = CodeUtils.getColumnType(columInfo.getType());

            Long columnTypeId = columnMap.get(dbColumnType);

            JavaType javaType = getJavaType(source, mapping, columnName, columnTypeId);


            String fieldCode = template.t_field.replace("[fieldType]", javaType.getShortName());

            if (javaType.isNeedImport()) {
                imports.add(javaType.getFullName());
            }

            String fieldName = CodeUtils.getFieldName(columnName);

            fieldNames.add(fieldName);
            fieldCode = fieldCode.replace("[fieldName]", fieldName);

            String comment = columInfo.getComment();

            if (StringUtils.isEmpty(comment)) {
                fieldCode = fieldCode.replace("[comment]", "");
            } else {
                fieldCode = fieldCode.replace("[comment]", "/**\n" +
                        "     * " + comment + "\n" +
                        "     */");
            }

            if (Objects.equals(columnName, source.getPrimaryKeyName())) {
                imports.add(ApplicationContant.config.getProperty("TableId"));
                fieldCode = fieldCode.replace("[annotations]", "@TableId");
            } else {
                fieldCode = fieldCode.replace("[annotations]", "");
            }

            sb.append(fieldCode);

            if (!source.getUseLombok()) {
                generateSetterCode(source.getChain(), javaType.getShortName(), source.getShortName(), fieldName, setterAndGetterCode);
                generateGetterCode(javaType.getShortName(), fieldName, setterAndGetterCode);
            }
        }
        return sb.toString();
    }

    private JavaType getJavaType(EntitySource source, Map<Long, Long> mapping, String columnName, Long columnTypeId) {

        //判断是不是主键
        if (Objects.equals(source.getPrimaryKeyName(), columnName) && javaFullTypeMap.containsKey(source.getPrimaryKeyType())) {
            return javaFullTypeMap.get(source.getPrimaryKeyType());
        }

        if (columnTypeId == null) {
            return defaultType;
        }

        Long javaTypeId = mapping.get(columnTypeId);
        if (javaTypeId != null && javaTypeMap.containsKey(javaTypeId)) {
            return javaTypeMap.get(javaTypeId);
        }


        return defaultType;
    }


}
