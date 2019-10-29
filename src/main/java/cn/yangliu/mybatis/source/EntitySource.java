package cn.yangliu.mybatis.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.EntitySetting;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.ex.HelperException;
import cn.yangliu.mybatis.tools.DBUtils;
import lombok.Data;

/**
 * The type Entity source.
 */
@Data
public class EntitySource extends AbstractCodeSource {

    private String baseClassFullName;

    private Boolean equalAndHash;

    private Boolean toString;

    private Boolean noArgConstructor;

    private Boolean chain;

    private Boolean useBaseClass;

    private String baseClassShortName;

    private String entityName;

    private String classSufix;

    private String baseClassPackage;

    private DBUtils.TableInfo tableInfo;

    private Map<String, String> mapping;

    private String primaryKeyName;

    private List<String> excludeColumns;

    private String primaryKeyType;

    private String dbType;

    private boolean singleTable;

    private String strategy;

    private Map<String, JavaType> columnMapping = new HashMap<>();

    private Map<String, JavaType> singleTableMapping;

    /**
     * Instantiates a new Entity source.
     *
     * @param projectSetting     the project setting
     * @param entitySetting      the entity setting
     * @param entityName         the entity name
     * @param tableInfo          the table info
     * @param dbType             the db type
     * @param singleTable        the single table
     * @param singleTableMapping the single table mapping
     */
    public EntitySource(ProjectSetting projectSetting, EntitySetting entitySetting, String entityName,
                        DBUtils.TableInfo tableInfo, String dbType, boolean singleTable,
                        Map<String, JavaType> singleTableMapping) {
        super(projectSetting, entitySetting.getEntityPackage(), projectSetting.getCodePath());
        this.singleTable = singleTable;
        this.tableInfo = tableInfo;
        this.singleTableMapping = singleTableMapping;
        this.dbType = dbType;
        this.strategy = entitySetting.getStrategy();
        this.mapping = EntitySetting.getMappings(entitySetting);
        this.primaryKeyName = entitySetting.getPrimaryKeyName();
        this.primaryKeyType = entitySetting.getPrimaryKeyType();
        this.entityName = entityName;
        this.classSufix = entitySetting.getClassSufix();
        init(entitySetting, entityName);
    }

    private void init(EntitySetting entitySetting, String entityName) {

        String[] split = entitySetting.getExcludeColumns().split(",");

        excludeColumns = new ArrayList<>(Arrays.asList(split));
        for (String s : split) {
            if (StringUtils.isNotEmpty(s)) {
                excludeColumns.add(s.toLowerCase());
                excludeColumns.add(s.toUpperCase());
            }
        }
        excludeColumns = new ArrayList<>(new HashSet<>(excludeColumns));

        equalAndHash = entitySetting.getEqualAndHash();

        toString = entitySetting.getToString();

        noArgConstructor = entitySetting.getNoArgConstructor();

        chain = entitySetting.getChain();

        useBaseClass = StringUtils.isNotEmpty(entitySetting.getBaseClassFullName());

        baseClassFullName = entitySetting.getBaseClassFullName();

        if (baseClassFullName.endsWith(ApplicationContant.PACKAGE_SEPARATOR)) {
            throw new HelperException("基类参数非法");
        }
        if (baseClassFullName.contains(ApplicationContant.PACKAGE_SEPARATOR)) {
            baseClassPackage = baseClassFullName.substring(0,
                    baseClassFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR));
            baseClassShortName =
                    baseClassFullName.substring(baseClassFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR) + 1);
        }

        filename = entityName + ".java";

        shortName = entityName;

        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + entityName;
        } else {
            classFullName = entityName;
        }
        setPrimaryKeyInfo(this);
    }


}
