package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import cn.yangliu.mybatis.bean.*;
import cn.yangliu.mybatis.gennerator.AbstractGenerator;
import cn.yangliu.mybatis.gennerator.GeneratorHandler;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.source.*;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import com.alibaba.fastjson.JSON;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * The type Generator controller.
 *
 * @author 杨柳
 * @date 2019 -01-06
 */
@Slf4j
@RestController
@JsonResponse
public class GeneratorController {

    /**
     * GeneratorHandler 代码生成主控制类
     */
    @Autowired
    private GeneratorHandler generatorHandler;

    @Autowired
    private LinkInfoService linkInfoService;


    /**
     * 生成代码
     *
     * @param request                     the request
     * @param singleTableMappingString the single table mapping string
     */
    @GetMapping("/generateCode")
    public void generateCode(Request request, String singleTableMappingString) {

         //当前连接信息
        LinkInfo linkInfo = linkInfoService.selectById(request.getId());
        //当前数据库信息
        List<Request.Database> databases = request.getDatabases();
        //选择的表信息
        List<DBUtils.TableInfo> tableInfos = new ArrayList<>();
        for (Request.Database database : databases) {
            List<Request.Database.Table> tables = database.getTables();
            if (tables == null || tables.isEmpty()) {
                continue;
            }
            List<String> tableNames = new ArrayList<>();
            for (Request.Database.Table table : tables) {
                tableNames.add(table.getTable());
            }

            //获取表信息 包括字段信息 类型 属性
            List<DBUtils.TableInfo> tablesInfo = DBUtils.getTablesInfo(linkInfo, database.getDatabase(), tableNames);
            tableInfos.addAll(tablesInfo);
        }


        //当前配置信息
        Settings settings = request.getSettings();

        //项目配置信息
        ProjectSetting projectSetting = settings.getProject();
        //实体配置信息
        EntitySetting entitySetting = settings.getEntity();
        //mapper配置信息
        MapperSetting mapperSetting = settings.getMapper();
        // repository 配置信息
        RepositorySetting repositorySetting = settings.getRepository();
        //service配置信息
        ServiceSetting serviceSetting = settings.getService();
        //controller配置信息
        ControllerSetting controllerSetting = settings.getController();

        List<Future<Boolean>> futures = new ArrayList<>();

        //是否是单表操作
        boolean singleTable = tableInfos.size() == 1;
        //字段映射信息
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


            //配置信息转换为对应的source实体
            EntitySource entitySource = new EntitySource(projectSetting, entitySetting, entityName,
                    tableInfo, linkInfo.getDatabaseType(), singleTable, mapping);
            RepositorySource repositorySource = new RepositorySource(projectSetting, repositorySetting, entitySource);
            MapperSource mapperSource = new MapperSource(projectSetting, mapperSetting, entitySource);
            ServiceImplSource serviceImplSource = new ServiceImplSource(projectSetting, serviceSetting,
                    repositorySource, mapperSource);
            ControllerSource controllerSource = new ControllerSource(projectSetting, controllerSetting, serviceImplSource);
            XmlSource xmlSource = new XmlSource(projectSetting, mapperSetting, mapperSource, entityName, linkInfo.getDatabaseType());
            //异步生成 加快响应速度
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

    /**
     * The type Single table mapping.
     */
    @Data
    public static class SingleTableMapping {
        private String column;
        private String javaType;
    }

}
