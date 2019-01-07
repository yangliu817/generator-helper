package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.EntitySetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.ex.HelperException;
import cn.yangliu.mybatis.tools.DBUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class EntitySource extends CodeSource {

    private String baseClassFullName;

    private Boolean equalAndHash;

    private Boolean toString;

    private Boolean noArgConstructor;

    private Boolean chain;

    private Boolean useBaseClass;

    private String baseClassShortName;

    private String baseClassPackage;

    private DBUtils.TableInfo tableInfo;

    private Map<String, String> mapping;

    private String primaryKeyName;

    private List<String> excludeColumns;

    private String primaryKeyType;

    private String dbType;

    public EntitySource(ProjectSetting projectSetting, EntitySetting entitySetting, String entityName, DBUtils.TableInfo tableInfo, String dbType) {
        super(projectSetting, entitySetting.getEntityPackage(), projectSetting.getCodePath());
        this.tableInfo = tableInfo;
        this.dbType = dbType;
        this.mapping = EntitySetting.getMappings(entitySetting);
        this.primaryKeyName = entitySetting.getPrimaryKeyName();
        this.primaryKeyType = entitySetting.getPrimaryKeyType();
        init(entitySetting, entityName);
    }

    private void init(EntitySetting entitySetting, String entityName) {

        String[] split = entitySetting.getExcludeColumns().split(",");

        excludeColumns = Arrays.asList(split);

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
            baseClassPackage = baseClassFullName.substring(0, baseClassFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR));
            baseClassShortName = baseClassFullName.substring(baseClassFullName.lastIndexOf(ApplicationContant.PACKAGE_SEPARATOR) + 1);
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
