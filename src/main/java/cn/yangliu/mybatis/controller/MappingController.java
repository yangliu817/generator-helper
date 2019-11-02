package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.enums.DBTypeEnum;
import cn.yangliu.mybatis.gennerator.AbstractGenerator;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.service.MappingSettingService;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * The type Mapping controller.
 *
 * @author 杨柳
 * @date 2019 -01-06
 */
@RestController
@JsonResponse
public class MappingController {

	@Autowired
	private MappingSettingService mappingSettingService;

	@Autowired
	private JavaTypeService javaTypeService;

	@Autowired
	private LinkInfoService linkInfoService;

	/**
	 * 获取表数据类型映射信息
	 *
	 * @param primaryKeyName the primary key name
	 * @param database       the database
	 * @param table          the table
	 * @param linkId         the link id
	 * @return string
	 */
	@GetMapping("/loadSingleTableMapping")
	public Map<String, Object> loadSingleTableMapping(String primaryKeyName, String database, String table,
                                                      Long linkId) {
		LinkInfo linkInfo = linkInfoService.selectById(linkId);
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
			SingleTableMapping mapping = new SingleTableMapping(columnName, columnType,
                    JavaType.newInstance(javaType));
			mappings.add(mapping);
		}

		List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());
		if (!Objects.equals(linkInfo.getDatabaseType(), DBTypeEnum.SQLSERVER.getDbType())) {
			javaTypes.remove(new JavaType("DateTimeOffset", "microsoft.sql.DateTimeOffset", true));
		}
		Map<String, Object> data = new HashMap<>(2);
		data.put("javaTypes", javaTypes);
		data.put("mappings", mappings);
		return data;
	}

	/**
	 * 获取默认的类型映射关系
	 *
	 * @param linkId linkId
	 * @return Map<String, ColumnType>
	 */
	@GetMapping("/getDefaultColumnTypeMap/{linkId}")
	public Map<String, ColumnType> getDefaultColumnTypeMap(@PathVariable("linkId") String linkId) {
		LinkInfo linkInfo = linkInfoService.selectById(linkId);
		return getDefaultColumnTypeMap(linkInfo);
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

	/**
	 * The type Single table mapping.
	 */
	@Data
	public static class SingleTableMapping {

		private String columnName;

		private String columnType;

		private JavaType javaType;

		/**
		 * Instantiates a new Single table mapping.
		 *
		 * @param columnName the column name
		 * @param columnType the column type
		 * @param javaType   the java type
		 */
		public SingleTableMapping(String columnName, String columnType, JavaType javaType) {
			this.columnName = columnName;
			this.columnType = columnType;
			this.javaType = javaType;
		}
	}

	/**
	 * 加载映射信息
	 *
	 * @param linkId    the linkId
	 * @param settingId the setting id
	 * @return string
	 */
	@GetMapping("/loadMappings")
	public Map<String, Object> loadMappings(Long linkId, Long settingId) {

		LinkInfo linkInfo = linkInfoService.selectById(linkId);

		MappingSetting queryPojo = new MappingSetting(settingId, linkInfo.getDatabaseType());

		List<MappingSetting> mappingSettings = mappingSettingService.loadMapping(queryPojo);

		List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());

		Map<String, Object> data = new HashMap<>(500);
		data.put("javaTypes", javaTypes);
		data.put("mappingSettings", mappingSettings);

		return data;
	}
}
