package cn.yangliu.mybatis.bean;

import cn.yangliu.mybatis.gennerator.AbstractGenerator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@TableName("t_entity_setting")
@NoArgsConstructor
public class EntitySetting {

    public EntitySetting(Long settingId) {
        this.settingId = settingId;
    }

    @TableId
    @JSONField(serialize = false)
    private Long id;

    @JSONField(serialize = false)
    private Long settingId;

    private String entityPackage;

    private String baseClassFullName;

    private Boolean equalAndHash;

    @JSONField(name = "m2String")
    private Boolean toString;

    private Boolean noArgConstructor;

    private Boolean chain;

    private String primaryKeyName;

    private String excludeColumns;

    private String primaryKeyType;

    private String strategy;

    /*@TableField(exist = false)
    private List<MappingSetting> mapping;*/

    @TableField(exist = false)
    private String mapping;

    public static List<MappingSetting> getMappingList(EntitySetting entitySetting,Long settingId,String dbType) {
        List<MappingSetting> mappingList = new ArrayList<>();
        HashMap<String,String> mappingData = getMappings(entitySetting);
        if (mappingData == null){
            return null;
        }
        Set<String> columnTypes = mappingData.keySet();
        for (String columnType : columnTypes) {
            String javaType = mappingData.get(columnType);
            Long columnTypeId = null;
            switch (dbType){
                case "mysql":
                    columnTypeId = AbstractGenerator.mysqlColumnMap.get(columnType).getId();
                    break;
                case "mariadb":
                    columnTypeId = AbstractGenerator.mariadbColumnMap.get(columnType).getId();
                    break;
                case "oracle":
                    columnTypeId = AbstractGenerator.oracleColumnMap.get(columnType).getId();
                    break;
                case "sqlserver":
                    columnTypeId = AbstractGenerator.sqlserverColumnMap.get(columnType).getId();
                    break;
                default:
                    break;
            }

            Long javaTypeId = AbstractGenerator.javaFullTypeMap.get(javaType).getId();
            MappingSetting mappingSetting = new MappingSetting(columnTypeId,javaTypeId,settingId);
            mappingList.add(mappingSetting);
        }
        return mappingList;
    }

    public static  HashMap<String,String> getMappings(EntitySetting entitySetting){
        return JSON.parseObject(entitySetting.getMapping(), HashMap.class);
    }

}
