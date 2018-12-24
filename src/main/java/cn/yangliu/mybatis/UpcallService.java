package cn.yangliu.mybatis;


import cn.yangliu.mybatis.bean.*;
import cn.yangliu.mybatis.gennerator.GeneratorHandler;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.service.MappingSettingService;
import cn.yangliu.mybatis.service.SettingsInfoService;
import cn.yangliu.mybatis.source.*;
import cn.yangliu.mybatis.tools.CodeUtils;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.FileUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Component
public class UpcallService extends de.felixroske.jfxsupport.UpcallService {

    @Autowired
    private LinkInfoService linkInfoService;

    @Autowired
    private SettingsInfoService settingsInfoService;

    @Autowired
    private MappingSettingService mappingSettingService;

    @Autowired
    private JavaTypeService javaTypeService;

    @Autowired
    private GeneratorHandler generatorHandler;

    public void loadLinks() {

        List<LinkInfo> linkInfos = linkInfoService.queryAll();

        if (linkInfos.size() > 0) {

            StringBuilder sb = new StringBuilder();

            String t_link = FileUtils.read(FileUtils.getFullPath("templates", "t_link.html"), false);

            for (LinkInfo linkInfo : linkInfos) {
                String linkHtml = t_link.replace("[link-id]", linkInfo.getId().toString());
                linkHtml = linkHtml.replace("[link-name]", linkInfo.getName());
                sb.append(linkHtml);
            }
            Platform.runLater(() -> {
                webEngine.executeScript("window.loadLinksCallback('" + sb.toString() + "')");
            });
        }

    }

    public void loadDatabases(String id) {
        LinkInfo linkInfo = linkInfoService.selectById(Long.parseLong(id));

        List<String> databases = DBUtils.getDatabases(linkInfo);

        if (databases.size() > 0) {
            StringBuilder sb = new StringBuilder();

            String t_databases = FileUtils.read(FileUtils.getFullPath("templates", "t_databases.html"), false);
            String t_table = FileUtils.read(FileUtils.getFullPath("templates", "t_table.html"), false);

            for (String dbname : databases) {
                String dbHtml = t_databases.replace("[database]", dbname);

                List<String> tables = DBUtils.getTables(linkInfo, dbname);
                StringBuilder t_sb = new StringBuilder();
                if (tables.size() > 0) {

                    for (String table : tables) {
                        String tableHtml = t_table.replace("[table]", table);
                        t_sb.append(tableHtml);
                    }

                }
                dbHtml = dbHtml.replace("[tables]", t_sb.toString());
                sb.append(dbHtml);
            }
            Platform.runLater(() -> {
                webEngine.executeScript("window.loadDatabasesCallback('" + id + "','" + sb.toString() + "')");
            });
        }
    }

    public void saveLink(String data) {

        LinkInfo linkInfo = JSON.parseObject(data, LinkInfo.class);

        linkInfoService.insert(linkInfo);


        String t_link = FileUtils.read(FileUtils.getFullPath("templates", "t_link.html"), false);

        String linkHtml = t_link.replace("[link-id]", linkInfo.getId().toString());
        linkHtml = linkHtml.replace("[link-name]", linkInfo.getName());

        String html = linkHtml;

        Platform.runLater(() -> {
            webEngine.executeScript("window.saveLinkCallback('" + html + "')");
        });
    }


    public void updateLink(String data) {
        LinkInfo linkInfo = JSON.parseObject(data, LinkInfo.class);
        String name = linkInfo.getName();
        linkInfo = linkInfoService.selectById(linkInfo.getId());
        linkInfo.setName(name);
        linkInfoService.updateById(linkInfo);
        String result = JSON.toJSONString(linkInfo);

        Platform.runLater(() -> {
            webEngine.executeScript("window.updateLinkCallback('" + result + "')");
        });
    }

    public void deleteLink(String id) {
        Long linkId = Long.parseLong(id);
        linkInfoService.deleteById(linkId);
        Platform.runLater(() -> {
            webEngine.executeScript("window.deleteLinkCallback('" + id + "')");
        });

    }

    public void saveSettings(String json) {
        SettingsInfo settingsInfo = JSON.parseObject(json, SettingsInfo.class);
        settingsInfoService.insert(settingsInfo);
        Platform.runLater(() -> {
            webEngine.executeScript("window.saveSettingsCallback('" + json + "')");
        });
    }

    public void loadSettings(String id) {
        Long linkId = Long.parseLong(id);
        SettingsInfo query = new SettingsInfo();
        query.setLinkId(linkId);

        List<SettingsInfo> settingsInfos = settingsInfoService.selectList(new EntityWrapper<>(query));
        String data = JSON.toJSONString(settingsInfos);
        Platform.runLater(() -> {
            webEngine.executeScript("window.loadSettingsCallback('" + data + "')");
        });
    }


    public void loadSettingDetail(String id) {
        SettingsInfo settingsInfo = settingsInfoService.selectById(Long.parseLong(id));
        Settings settings = settingsInfo.getSettings();
        String data = JSON.toJSONString(settings);

        Platform.runLater(() -> {
            webEngine.executeScript("window.loadSettingDetailCallback('" + data + "')");
        });

    }


    public void loadDefaultMappings(String id) {
        Long linkId = Long.parseLong(id);
        LinkInfo linkInfo = linkInfoService.selectById(linkId);

        MappingSetting queryPojo = new MappingSetting(0L, linkInfo.getDatabaseType());
        List<MappingSetting> mappingSettings = mappingSettingService.loadMapping(queryPojo);

        String t_mapping = FileUtils.read(FileUtils.getFullPath("templates", "t_mapping.html"), false);
        String t_mapping_option = FileUtils.read(FileUtils.getFullPath("templates", "t_mapping_option.html"), false);

        StringBuilder options = new StringBuilder();

        List<JavaType> javaTypes = javaTypeService.selectList(new EntityWrapper<>());

        for (JavaType javaType : javaTypes) {
            String optionHtml = t_mapping_option.replace("[java-type]", javaType.getFullName()).replace("[java-type-id]", javaType.getId().toString());
            options.append(optionHtml);
        }

        StringBuilder sb = new StringBuilder();
        for (MappingSetting mappingSetting : mappingSettings) {

            String mappingHtml = t_mapping.replace("[column-type]", mappingSetting.getColumnType());
            mappingHtml = mappingHtml.replace("[column-type-id]", mappingSetting.getColumnTypeId().toString());
            mappingHtml = mappingHtml.replace("[java-type]", mappingSetting.getFullName());
            mappingHtml = mappingHtml.replace("[java-type-id]", mappingSetting.getJavaTypeId().toString());
            mappingHtml = mappingHtml.replace("[java-type-options]", options.toString());
            sb.append(mappingHtml);
        }
        Platform.runLater(() -> {
            webEngine.executeScript("window.loadDefaultMappingsCallback('" + sb.toString() + "')");
        });
    }

    public void createCode(String data) {
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
        ServiceSetting serviceSetting = settings.getService();
        ControllerSetting controllerSetting = settings.getController();

        List<Future<Boolean>> futures = new ArrayList<>();
        tableInfos.parallelStream().forEach(tableInfo -> {

            String tableName = tableInfo.getName();

            String tablePrefix = request.getSettings().getProject().getTablePrefix();

            String entityName = CodeUtils.getClassName(tableName, tablePrefix);

            EntitySource entitySource = new EntitySource(projectSetting, entitySetting, entityName, tableInfo);
            MapperSource mapperSource = new MapperSource(projectSetting, mapperSetting, entitySource);
            ServiceImplSource serviceImplSource = new ServiceImplSource(projectSetting, serviceSetting, mapperSource);
            ControllerSource controllerSource = new ControllerSource(projectSetting, controllerSetting, serviceImplSource);
            XmlSource xmlSource = new XmlSource(projectSetting, mapperSetting, mapperSource, entityName);
            Future<Boolean> future = generatorHandler.doGenerator(projectSetting, entitySource, mapperSource, xmlSource, serviceImplSource, controllerSource);
            futures.add(future);
        });


        futures.forEach(f->{
            try{
                f.get();

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        });

        Platform.runLater(() -> {
            webEngine.executeScript("window.createCodeCallback()");
        });


    }
}
