package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.source.XmlSource;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class XmlGenerator implements Generator<XmlSource> {

    private String t_mapper;
    private String t_mapper_if;
    private String t_mapper_where_if;
    private String t_mapper_where;
    private String deleteById;
    private String deleteByIds;
    private String insert;
    private String insertList;
    private String insertSelect;
    private String selectById;
    private String selectList;
    private String selectOne;
    private String updateById;
    private String updateSelectById;
    private String t_mapper_select_key;
    private String t_mapper_foreach;
    private String t_mapper_result;

    public XmlGenerator() {
        t_mapper = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper.xml"), true);
        t_mapper_foreach = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_foreach.xml"), true);
        t_mapper_if = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_if.xml"), true);
        t_mapper_where_if = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_if.xml"), "    ", true);
        t_mapper_where = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_where.xml"), true);
        t_mapper_result = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_result.xml"), true);
        deleteById = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "deleteById.xml"), true);
        deleteByIds = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "deleteByIds.xml"), true);
        insert = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "insert.xml"), true);
        insertList = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "insertList.xml"), true);
        insertSelect = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "insertSelect.xml"), true);
        selectById = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "selectById.xml"), true);
        selectList = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "selectList.xml"), true);
        selectOne = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "selectOne.xml"), true);
        updateById = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "updateById.xml"), true);
        updateSelectById = FileUtils.read(FileUtils.getFullPath("templates/xml/methods", "updateSelectById.xml"), true);
        t_mapper_select_key = FileUtils.read(FileUtils.getFullPath("templates/xml", "t_mapper_select_key.xml"), true);
    }

    @Override
    public void generate(XmlSource source) {
        String xmlCode = t_mapper;
        List<String> excludeColumns = source.getEntitySource().getExcludeColumns();

        DBUtils.TableInfo tableInfo = source.getEntitySource().getTableInfo();

        String primaryKeyName = source.getEntitySource().getPrimaryKeyName();
        List<String> columns = tableInfo.getColumInfos().stream()
                .filter(column -> !excludeColumns.contains(column.getName()) &&
                        !Objects.equals(primaryKeyName, column.getName()))
                .map(DBUtils.ColumInfo::getName).collect(Collectors.toList());
        List<String> fields = tableInfo.getColumInfos().stream()
                .filter(column -> !excludeColumns.contains(column.getName()) &&
                        !Objects.equals(primaryKeyName, column.getName()))
                .map(columInfo -> CodeUtils.getFieldName(columInfo.getName()))
                .collect(Collectors.toList());

        boolean containsPrimaryKey = source.isContainsPrimaryKey();
        boolean primaryKeyTypeIsString = Objects.equals(source.getEntitySource().getPrimaryKeyType(), "java.lang.String");

        xmlCode = generateColumnSQL(columns, containsPrimaryKey, primaryKeyName, xmlCode);
        xmlCode = generateMapping(primaryKeyName, containsPrimaryKey, columns, fields, xmlCode);
        xmlCode = xmlCode.replace("[mapperClassFullName]", source.getMapperFullName());

        if (source.isMybatisPlus()) {
            xmlCode = xmlCode.replace("[methods]", "");
            FileUtils.output(xmlCode, source.getFilepath(), source.getFilename());
            return;
        }

        xmlCode = generateMethods(source, xmlCode, tableInfo, primaryKeyName, columns, fields, containsPrimaryKey, primaryKeyTypeIsString);

        FileUtils.output(xmlCode, source.getFilepath(), source.getFilename());
    }

    protected String generateMethods(XmlSource source, String xmlCode, DBUtils.TableInfo tableInfo, String primaryKeyName,
                                     List<String> columns, List<String> fields, boolean containsPrimaryKey, boolean primaryKeyTypeIsString) {
        String entityClassFullName = source.getEntitySource().getClassFullName();
        xmlCode = xmlCode.replace("[entityClassFullName]", entityClassFullName);
        String tableName = tableInfo.getName();
        String methodCode = "";
        String insertCode = generateInsert(containsPrimaryKey, primaryKeyTypeIsString, primaryKeyName, columns, fields) + "\n";
        String insertSelectCode = generateInsertSelect(containsPrimaryKey, primaryKeyTypeIsString, primaryKeyName, columns, fields) + "\n";
        String insertListCode = generateInsertList(containsPrimaryKey, primaryKeyTypeIsString, primaryKeyName, columns, fields) + "\n";
        methodCode = methodCode + insertCode + insertSelectCode + insertListCode;
        if (source.isContainsPrimaryKey()) {
            String updateByIdCode = generateUpdateById(columns, fields) + "\n";
            String uspdateSelectByIdCode = generateUspdateSelectById(columns, fields) + "\n";
            String deleteByIdCode = generateDeleteById() + "\n";
            String deleteByIdsCode = generateDeleteByIds() + "\n";
            String selectByIdCode = generateSelectById() + "\n";
            methodCode = methodCode + updateByIdCode + uspdateSelectByIdCode + deleteByIdCode + deleteByIdsCode + selectByIdCode;
        }
        String selectOneCode = generateSelectOne(columns, fields) + "\n";
        String selectListCode = generateSelectList(columns, fields);
        methodCode = methodCode + selectOneCode + selectListCode;
        String selectKeyCode = "";
        if (containsPrimaryKey && !primaryKeyTypeIsString) {
            selectKeyCode = t_mapper_select_key;
        }
        methodCode = methodCode.replace("[selectKey]", selectKeyCode);

        methodCode = methodCode.replace("[tableName]", tableName)
                .replace("[primaryKeyType]", source.getEntitySource().getPrimaryKeyType())
                .replace("[primaryKeyName]", primaryKeyName)
                .replace("[primaryKeyFieldName]", CodeUtils.getFieldName(primaryKeyName))
                .replace("[entityClassFullName]", entityClassFullName);
        xmlCode = xmlCode.replace("[methods]", methodCode);
        return xmlCode;
    }


    protected String generateColumnSQL(List<String> columns, boolean containsPrimaryKey, String primaryKeyName, String code) {
        String columnString = columns.toString().replace("[", "").replace("]", "");
        if (containsPrimaryKey) {
            columnString = primaryKeyName + ", " + columnString;
        }
        code = code.replace("[ColumnSQL]", columnString);
        return code;
    }

    protected String generateInsertSelect(boolean containsPrimaryKey, boolean primaryKeyTypeIsString,
                                          String primaryKeyName, List<String> columns, List<String> fields) {

        StringBuilder columnsBuilder = new StringBuilder();
        StringBuilder fieldsBuilder = new StringBuilder();
        if (containsPrimaryKey && primaryKeyTypeIsString) {
            String columnIfCode = t_mapper_if.replace("[fieldName]", CodeUtils.getFieldName(primaryKeyName));
            String fieldIfCode = columnIfCode;
            columnIfCode = columnIfCode.replace("[content]", primaryKeyName + ",");
            fieldIfCode = fieldIfCode.replace("[content]", "#{" + CodeUtils.getFieldName(primaryKeyName) + "},");
            columnsBuilder.append(columnIfCode);
            fieldsBuilder.append(fieldIfCode);
        }
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            String fieldName = fields.get(i);
            String columnIfCode = t_mapper_if.replace("[fieldName]", fieldName);
            String fieldIfCode = columnIfCode;
            if (columns.size() == (i + 1)) {
                columnIfCode = columnIfCode.replace("[content]", columnName);
                fieldIfCode = fieldIfCode.replace("[content]", "#{" + fieldName + "}");
            } else {
                columnIfCode = columnIfCode.replace("[content]", columnName + ",");
                fieldIfCode = fieldIfCode.replace("[content]", "#{" + fieldName + "},");
            }

            columnsBuilder.append(columnIfCode);
            fieldsBuilder.append(fieldIfCode);
        }
        String insertSelectCode = insertSelect.replace("[columnIfList]", columnsBuilder.toString());
        insertSelectCode = insertSelectCode.replace("[fieldIfList]", fieldsBuilder.toString());
        return insertSelectCode;
    }


    protected String generateUpdateById(List<String> columns, List<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            String fieldName = fields.get(i);
            sb.append(columnName).append(" = ").append("#{").append(fieldName).append("}");
            if ((i + 1) != columns.size()) {
                sb.append(",");
            }
        }
        return updateById.replace("[column2field]", sb.toString());
    }

    protected String generateInsertList(boolean containsPrimaryKey, boolean primaryKeyTypeIsString,
                                        String primaryKeyName, List<String> columns, List<String> fields) {
        String columnCode = columns.toString().replace("[", "").replace("]", "");
        String foreachCodeFieldCode = fields.toString().replace("[", "").replace("]", "");
        if (containsPrimaryKey && primaryKeyTypeIsString) {
            columnCode = primaryKeyName + ", " + columnCode;
            foreachCodeFieldCode = CodeUtils.getFieldName(primaryKeyName) + ", " + foreachCodeFieldCode;
        }
        foreachCodeFieldCode = foreachCodeFieldCode.replace(", ", "}, #{item.");
        foreachCodeFieldCode = "#{item." + foreachCodeFieldCode + "}";

        String foreachCode = t_mapper_foreach.replace("[fields]", foreachCodeFieldCode);
        String insertListCode = insertList.replace("[foreach]", foreachCode);
        insertListCode = insertListCode.replace("[columns]", columnCode);
        return insertListCode;
    }

    protected String generateSelectList(List<String> columns, List<String> fields) {
        return generateSelectCode(columns, fields, selectList);
    }

    protected String generateSelectCode(List<String> columns, List<String> fields, String replaceTemplate) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            String fieldName = fields.get(i);
            String tIfCode = t_mapper_where_if.replace("[fieldName]", fieldName);
            tIfCode = tIfCode.replace("[content]", "and " + columnName + " = #{" + fieldName + "}");
            sb.append(tIfCode);
        }
        String whereCode = t_mapper_where.replace("[ifList]", sb.toString());
        return replaceTemplate.replace("[where]", whereCode);
    }

    protected String generateSelectOne(List<String> columns, List<String> fields) {
        return generateSelectCode(columns, fields, selectOne);
    }

    protected String generateSelectById() {
        return selectById;
    }

    protected String generateDeleteById() {
        return deleteById;
    }

    protected String generateDeleteByIds() {
        return deleteByIds;
    }

    protected String generateUspdateSelectById(List<String> columns, List<String> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            String fieldName = fields.get(i);
            String content = columnName + " = " + "#{" + fieldName + "}";
            if ((i + 1) != columns.size()) {
                content = content + ",";
            }
            String columnCode = t_mapper_if.replace("[fieldName]", fieldName)
                    .replace("[content]", content);
            sb.append(columnCode);
        }
        return updateSelectById.replace("[ifList]", sb.toString());
    }

    protected String generateMapping(String primaryKeyName, boolean containsPrimaryKey, List<String> columns, List<String> fields, String code) {
        StringBuilder sb = new StringBuilder();
        if (containsPrimaryKey) {
            String idMapping = t_mapper_result.replace("[columnName]", primaryKeyName)
                    .replace("[type]", "id")
                    .replace("[fieldName]", CodeUtils.getFieldName(primaryKeyName));
            sb.append(idMapping);
        }

        for (int i = 0; i < columns.size(); i++) {
            String columnName = columns.get(i);
            String fieldName = fields.get(i);
            String columnMapping = t_mapper_result.replace("[columnName]", columnName)
                    .replace("[type]", "result")
                    .replace("[fieldName]", fieldName);
            sb.append(columnMapping);
        }

        code = code.replace("[mapping]", sb.toString());
        return code;
    }

    /**
     * 全字段新增
     */
    protected String generateInsert(boolean containsPrimaryKey, boolean primaryKeyTypeIsString,
                                    String primaryKeyName, List<String> columns, List<String> fields) {
        String columsString = columns.toString().replace("[", "").replace("]", "");
        String fieldsString = fields.stream().map(f -> "#{" + f + "}").collect(Collectors.toList())
                .toString().replace("[", "").replace("]", "");

        if (containsPrimaryKey && primaryKeyTypeIsString) {
            columsString = primaryKeyName + ", " + columsString;
            fieldsString = "#{" + CodeUtils.getFieldName(primaryKeyName) + "}, " + fieldsString;
        }
        return insert.replace("[columns]", columsString).replace("[fields]", fieldsString);
    }
}
