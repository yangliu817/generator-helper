package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.bean.*;
import cn.yangliu.mybatis.gennerator.AbstractGenerator;
import cn.yangliu.mybatis.gennerator.GeneratorHandler;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.source.*;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import com.alibaba.fastjson.JSON;
import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.annotations.MappingController;
import de.felixroske.jfxsupport.web.AbstractController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author 杨柳
 * @date 2019-01-06
 */
@MappingController
@Slf4j
public class GeneratorController extends AbstractController {

    @Autowired
    private GeneratorHandler generatorHandler;

    @Autowired
    private LinkInfoService linkInfoService;


    @Mapping("/generateCode")
    public void generateCode(String data, String singleTableMappingString) {

        Request request = JSON.parseObject(data, Request.class);

        LinkInfo linkInfo = linkInfoService.selectById(request.getId());
        List<Request.Database> databases = request.getDatabases();
        List<DBUtils.TableInfo> tableInfos = new ArrayList<>();
        for (Request.Database database : databases) {
            List<Request.Database.Table> tables = database.getTables();
            if (tables == null || tables.size() == 0) {
                continue;
            }
            List<String> tableNames = new ArrayList<>();
            for (Request.Database.Table table : tables) {
                tableNames.add(table.getTable());
            }

            List<DBUtils.TableInfo> tablesInfo = DBUtils.getTablesInfo(linkInfo, database.getDatabase(), tableNames);
            tableInfos.addAll(tablesInfo);
        }


        Settings settings = request.getSettings();
        ProjectSetting projectSetting = settings.getProject();
        EntitySetting entitySetting = settings.getEntity();
        MapperSetting mapperSetting = settings.getMapper();
        RepositorySetting repositorySetting = settings.getRepository();
        ServiceSetting serviceSetting = settings.getService();
        ControllerSetting controllerSetting = settings.getController();

        List<Future<Boolean>> futures = new ArrayList<>();

        boolean singleTable = tableInfos.size() == 1;
        Map<String, JavaType> mapping = new HashMap<>(1000);
        if (singleTable) {
            List<SingleTableMapping> mappings = JSON.parseArray(singleTableMappingString, SingleTableMapping.class);
            for (SingleTableMapping m : mappings) {
                mapping.put(m.column, AbstractGenerator.javaFullTypeMap.getOrDefault(m.javaType, JavaType.DEFAULT));
            }
        }

        tableInfos.parallelStream().forEach(tableInfo -> {

            String tableName = tableInfo.getName();

            String tablePrefix = request.getSettings().getProject().getTablePrefix();

            String entityName = CodeUtils.getClassName(tableName, tablePrefix) + entitySetting.getClassSufix();


            EntitySource entitySource = new EntitySource(projectSetting, entitySetting, entityName,
                    tableInfo, linkInfo.getDatabaseType(), singleTable, mapping);
            RepositorySource repositorySource = new RepositorySource(projectSetting, repositorySetting, entitySource);
            MapperSource mapperSource = new MapperSource(projectSetting, mapperSetting, entitySource);
            ServiceImplSource serviceImplSource = new ServiceImplSource(projectSetting, serviceSetting,
                    repositorySource, mapperSource);
            ControllerSource controllerSource = new ControllerSource(projectSetting, controllerSetting, serviceImplSource);
            XmlSource xmlSource = new XmlSource(projectSetting, mapperSetting, mapperSource, entityName, linkInfo.getDatabaseType());
            Future<Boolean> future = generatorHandler.doGenerator(projectSetting, entitySource, repositorySource, mapperSource, xmlSource,
                    serviceImplSource, controllerSource);
            futures.add(future);
        });


        futures.forEach(f -> {
            try {
                f.get();

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    @Data
    public static class SingleTableMapping {
        private String column;
        private String javaType;
    }

}
