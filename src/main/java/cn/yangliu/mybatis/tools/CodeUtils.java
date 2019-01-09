package cn.yangliu.mybatis.tools;

import cn.yangliu.comm.tools.StringUtils;

public class CodeUtils {

    public static String getClassName(String tableName, String tablePrefix) {
        tableName = tableName.toLowerCase();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(tablePrefix)) {
            if (tableName.startsWith(tablePrefix)) {
                tableName = tableName.replaceFirst(tablePrefix, "");
            }
        }
        String[] array = tableName.split("_");
        for (String s : array) {
            sb.append(firstChar2Uppercase(s));
        }
        return sb.toString();
    }

    public static String getColumnType(String source) {
        if (source.contains("(")) {
            return source.replaceAll("\\d+", "").replace("(", "").replace(")", "").toLowerCase();
        }
        return source;
    }

    public static String getFieldName(String columName) {
        columName = columName.toLowerCase();
        StringBuilder sb = new StringBuilder();
        String[] array = columName.split("_");
        for (String s : array) {
            sb.append(firstChar2Lowercase(s));
        }
        return sb.toString();
    }

    public static String firstChar2Uppercase(String source) {
        if (StringUtils.isEmpty(source)) {
            throw new NullPointerException();
        }
        String firstChar = source.substring(0, 1);
        String remain = source.substring(1);
        return firstChar.toUpperCase() + remain;
    }

    public static String firstChar2Lowercase(String source) {
        if (StringUtils.isEmpty(source)) {
            throw new NullPointerException();
        }
        String firstChar = source.substring(0, 1);
        String remain = source.substring(1);
        return firstChar.toLowerCase() + remain;
    }
}
