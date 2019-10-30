package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.service.LinkInfoService;
import cn.yangliu.mybatis.tools.DBUtils;
import com.alibaba.fastjson.JSON;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Link info controller.
 */
@Slf4j
@RestController
@JsonResponse
public class LinkInfoController {

    @Autowired
    private LinkInfoService linkInfoService;

    /**
     * 加载 连接信息
     *
     * @return string
     */
    @GetMapping("/loadLinks")
    public List<LinkInfo> loadLinks() {
        return linkInfoService.queryAll();
    }

    /**
     * 加载数据库信息
     *
     * @param linkInfo the link info
     * @return string
     */
    @GetMapping("/loadDatabases")
    public List<DBUtils.DatabaseInfo> loadDatabases(LinkInfo linkInfo) {

        List<DBUtils.DatabaseInfo> databases = DBUtils.getDatabases(linkInfo);

        if (!databases.isEmpty()) {
            databases =
                    databases.stream().sorted(Comparator.comparing(DBUtils.DatabaseInfo::getName)).collect(Collectors.toList());
            for (DBUtils.DatabaseInfo database : databases) {
                List<DBUtils.TableInfo> tables = DBUtils.getTables(linkInfo, database.getName());
                tables =
                        tables.stream().sorted(Comparator.comparing(DBUtils.TableInfo::getName)).collect(Collectors.toList());
                database.setTables(tables);
            }
        }
        return databases;
    }

    /**
     * 保存连接
     *
     * @param linkInfo the linkInfo
     * @return string
     */
    @PostMapping("/saveLink")
    public LinkInfo saveLink(@RequestBody LinkInfo linkInfo) {
        linkInfoService.insert(linkInfo);
        return linkInfo;
    }

    /**
     * 更新连接信息
     *
     * @param linkInfo the linkInfo
     */
    @PutMapping("/updateLink")
    public LinkInfo updateLink(@RequestBody LinkInfo linkInfo) {
        String name = linkInfo.getName();
        linkInfo = linkInfoService.selectById(linkInfo.getId());
        linkInfo.setName(name);
        linkInfoService.updateById(linkInfo);
        return linkInfo;
    }

    /**
     * 删除连接
     *
     * @param id the id
     */
    @DeleteMapping("/deleteLink/{id}")
    public boolean deleteLink(@PathVariable("id") String id) {
        Long linkId = Long.parseLong(id);
        return linkInfoService.deleteById(linkId);
    }

}
