package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.tools.DBUtils;
import com.alibaba.fastjson.JSON;
import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.annotations.MappingController;
import de.felixroske.jfxsupport.web.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@MappingController
public class LinkInfoController extends AbstractController {

    @Autowired
    private LinkInfoService linkInfoService;

    @Mapping("/loadLinks")
    public String loadLinks() {
        List<LinkInfo> linkInfos = linkInfoService.queryAll();
        return JSON.toJSONString(linkInfos);
    }

    @Mapping("/loadDatabases")
    public String loadDatabases(String linkInfoString) {

        LinkInfo linkInfo = JSON.parseObject(linkInfoString, LinkInfo.class);

        List<DBUtils.DatabaseInfo> databases = DBUtils.getDatabases(linkInfo);

        if (databases.size() > 0) {
            databases = databases.stream().sorted(Comparator.comparing(DBUtils.DatabaseInfo::getName)).collect(Collectors.toList());
            for (DBUtils.DatabaseInfo database : databases) {
                List<DBUtils.TableInfo> tables = DBUtils.getTables(linkInfo, database.getName());
                tables = tables.stream().sorted(Comparator.comparing(DBUtils.TableInfo::getName)).collect(Collectors.toList());
                database.setTables(tables);
            }
        }
        return JSON.toJSONString(databases);
    }

    @Mapping("/saveLink")
    public String saveLink(String data) {

        LinkInfo linkInfo = JSON.parseObject(data, LinkInfo.class);
        linkInfoService.insert(linkInfo);

        return JSON.toJSONString(linkInfo);
    }

    @Mapping("/updateLink")
    public void updateLink(String data) {
        LinkInfo linkInfo = JSON.parseObject(data, LinkInfo.class);
        String name = linkInfo.getName();
        linkInfo = linkInfoService.selectById(linkInfo.getId());
        linkInfo.setName(name);
        linkInfoService.updateById(linkInfo);
    }

    @Mapping("/deleteLink")
    public void deleteLink(String id) {
        Long linkId = Long.parseLong(id);
        linkInfoService.deleteById(linkId);
    }

}
