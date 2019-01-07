package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨柳
 * @date 2019-01-07
 */
@Data
public abstract class AbstractSource implements Source{

    protected boolean containsPrimaryKey;

    protected String primaryKeyFieldName;

    protected String primaryKeyName;

    protected String primaryKeyType;

    protected void setPrimaryKeyInfo(EntitySource entitySource) {
        DBUtils.TableInfo tableInfo = entitySource.getTableInfo();
        List<String> excludeColumns = entitySource.getExcludeColumns();
        List<String> allColumns = tableInfo.getColumInfos().stream()
                .filter(column -> !excludeColumns.contains(column.getName()))
                .map(DBUtils.ColumInfo::getName).collect(Collectors.toList());
        this.containsPrimaryKey = allColumns.contains(entitySource.getPrimaryKeyName());
        this.primaryKeyFieldName = CodeUtils.getFieldName(entitySource.getPrimaryKeyName());
        this.primaryKeyType = entitySource.getPrimaryKeyType();
        this.primaryKeyName = entitySource.getPrimaryKeyName();
    }
}
