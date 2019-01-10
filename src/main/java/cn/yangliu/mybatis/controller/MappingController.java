package cn.yangliu.mybatis.controller;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.enums.DBTypeEnum;
import cn.yangliu.mybatis.gennerator.AbstractGenerator;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.service.MappingSettingService;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.web.AbstractController;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author 杨柳
 * @date 2019-01-06
 */
@de.felixroske.jfxsupport.annotations.MappingController
public class MappingController extends AbstractController {

    @Autowired
    private MappingSettingService mappingSettingService;

    @Autowired
    private JavaTypeService javaTypeService;

    @Mapping("/loadSingleTableMapping")
    public String loadSingleTableMapping(String primaryKeyName, String database, String table, String linkInfoString) {
        LinkInfo linkInfo = JSON.parseObject(linkInfoString, LinkInfo.class);
        List<DBUtils.TableInfo> tableInfos = DBUtils.getTablesInfo(linkInfo, database, Arrays.asList(table));
        Map<String, ColumnType> columnTypeMap = getDefaultColumnTypeMap(linkInfo);

        List<SingleTableMapping> mappings = new ArrayList<>();

        List<DBUtils.ColumInfo> columInfos = tableInfos.get(0).getColumInfos();
        for (DBUtils.ColumInfo columInfo : columInfos) {
            String columnName = columInfo.getName();
            if (Objects.equals(primaryKeyName, columnName)) {
                continue;
            }
            String columnType = CodeUtils.getColumnType(columInfo.getType());
            ColumnType ct = columnTypeMap.get(columnType);
            JavaType javaType;
            if (Objects.isNull(ct)) {
                javaType = JavaType.DEFAULT;
            } else {
                javaType = AbstractGenerator.column2javaTypeMap.getOrDefault(ct, JavaType.DEFAULT);
            }
            SingleTableMapping mapping = new SingleTableMapping(columnName, columnType, JavaType.newInstance(javaType));
            mappings.add(mapping);
        }

        List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());
        if (!Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.SQLSERVER.getDbType())) {
            javaTypes.remove(new JavaType("DateTimeOffset", "microsoft.sql.DateTimeOffset", true));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("javaTypes", javaTypes);
        data.put("mappings", mappings);
        return JSON.toJSONString(data);
    }

    private static Map<String, ColumnType> getDefaultColumnTypeMap(LinkInfo linkInfo) {
        if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.MYSQL.getDbType())) {
            return AbstractGenerator.mysqlColumnMap;
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.ORACLE.getDbType())) {
            return AbstractGenerator.oracleColumnMap;
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.MARIADB.getDbType())) {
            return AbstractGenerator.mariadbColumnMap;
        } else if (Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.SQLSERVER.getDbType())) {
            return AbstractGenerator.sqlserverColumnMap;
        }
        throw new RuntimeException();
    }

    @Data
    public static class SingleTableMapping {

        private String columnName;

        private String columnType;

        private JavaType javaType;

        public SingleTableMapping(String columnName, String columnType, JavaType javaType) {
            this.columnName = columnName;
            this.columnType = columnType;
            this.javaType = javaType;
        }
    }

    @Mapping("/loadMappings")
    public String loadMappings(String json, String settingId) {

        LinkInfo linkInfo = JSON.parseObject(json, LinkInfo.class);

        long id = 0L;
        if (StringUtils.isNotEmpty(settingId)) {
            id = Long.parseLong(settingId);
        }
        MappingSetting queryPojo = new MappingSetting(id, linkInfo.getDatabaseType());

        List<MappingSetting> mappingSettings = mappingSettingService.loadMapping(queryPojo);

        List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());

        Map<String, Object> data = new HashMap<>();
        data.put("javaTypes", javaTypes);
        data.put("mappingSettings", mappingSettings);

        return JSON.toJSONString(data);
    }
}
