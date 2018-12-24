package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.EntitySetting;
import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.ex.HelperException;
import cn.yangliu.mybatis.tools.DBUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

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

    private List<MappingSetting> mapping;

    private String primaryKeyName;

    private List<String> excludeColumns;

    private String primaryKeyType;

    public EntitySource(ProjectSetting projectSetting, EntitySetting entitySetting, String entityName, DBUtils.TableInfo tableInfo) {
        super(projectSetting, entitySetting.getEntityPackage(),projectSetting.getCodePath());
        this.tableInfo = tableInfo;
        this.mapping = entitySetting.getMapping();
        this.primaryKeyName = entitySetting.getPrimaryKeyName();
        this.primaryKeyType = entitySetting.getPrimaryKeyType();
        init(entitySetting,entityName);
    }

    private void init(EntitySetting entitySetting,String entityName) {

        String[] split = entitySetting.getExcludeColumns().split(",");

        excludeColumns = Arrays.asList(split);

        equalAndHash = entitySetting.getEqualAndHash();

        toString = entitySetting.getToString();

        noArgConstructor = entitySetting.getNoArgConstructor();

        chain = entitySetting.getChain();

        useBaseClass = entitySetting.getUseBaseClass();

        baseClassFullName = entitySetting.getBaseClassFullName();

        if (baseClassFullName.endsWith(".")) {
            throw new HelperException("基类参数非法");
        }
        if (baseClassFullName.contains(".")) {
            baseClassPackage = baseClassFullName.substring(0, baseClassFullName.lastIndexOf("."));
            baseClassShortName = baseClassFullName.substring(baseClassFullName.lastIndexOf(".") + 1);
        }

        filename = entityName + ".java";

        shortName = entityName;

        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + "." + entityName;
        } else {
            classFullName = entityName;
        }
    }


}
