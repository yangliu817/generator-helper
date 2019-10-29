package cn.yangliu.mybatis.tools;

import cn.yangliu.comm.tools.StringUtils;
import org.springframework.util.Assert;

/**
 * The type Code utils.
 */
public class CodeUtils {

    /**
     * Gets class name.
     *
     * @param tableName   the table name
     * @param tablePrefix the table prefix
     * @return the class name
     */
    public static String getClassName(String tableName, String tablePrefix) {
        tableName = tableName.toLowerCase();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(tablePrefix)) {
            if (tableName.startsWith(tablePrefix)||tableName.startsWith(tablePrefix.toLowerCase())||tableName.startsWith(tablePrefix.toUpperCase())) {
                tableName = tableName.replaceFirst(tablePrefix, "");
                tableName = tableName.replaceFirst(tablePrefix.toLowerCase(), "");
                tableName = tableName.replaceFirst(tablePrefix.toUpperCase(), "");
            }
        }
        String[] array = tableName.split("_");
        for (String s : array) {
            sb.append(firstChar2Uppercase(s));
        }
        return sb.toString();
    }

    /**
     * Gets column type.
     *
     * @param source the source
     * @return the column type
     */
    public static String getColumnType(String source) {
        if (source.contains("(")) {
            return source.replaceAll("\\d+", "").replace("(", "").replace(")", "").toLowerCase();
        }
        return source;
    }

    /**
     * Gets field name.
     *
     * @param columName the colum name
     * @return the field name
     */
    public static String getFieldName(String columName) {
        columName = columName.toLowerCase();
        StringBuilder sb = new StringBuilder();
        String[] array = columName.split("_");
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                sb.append(firstChar2Lowercase(array[i]));
                continue;
            }
            sb.append(firstChar2Uppercase(array[i]));
        }
        return sb.toString();
    }

    /**
     * First char 2 uppercase string.
     *
     * @param source the source
     * @return the string
     */
    public static String firstChar2Uppercase(String source) {
        Assert.hasText(source, "source不能为空");
        if (source.length() == 1) {
            return source.toUpperCase();
        }
        String firstChar = source.substring(0, 1);
        String remain = source.substring(1);
        return firstChar.toUpperCase() + remain;
    }

    /**
     * First char 2 lowercase string.
     *
     * @param source the source
     * @return the string
     */
    public static String firstChar2Lowercase(String source) {
        Assert.hasText(source, "source不能为空");
        if (source.length() == 1) {
            return source.toLowerCase();
        }
        String firstChar = source.substring(0, 1);
        String remain = source.substring(1);
        return firstChar.toLowerCase() + remain;
    }
}
