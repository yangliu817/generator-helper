package cn.yangliu.mybatis.controller;

import java.util.List;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import cn.yangliu.mybatis.bean.Settings;
import cn.yangliu.mybatis.bean.SettingsInfo;
import cn.yangliu.mybatis.service.SettingsInfoService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Setting controller.
 *
 * @author 杨柳
 * @date 2019 -01-06
 */
@RestController
@JsonResponse
public class SettingController {

    @Autowired
    private SettingsInfoService settingsInfoService;

    /**
     * 保存配置信息
     *
     * @param settingsInfo the settingsInfo
     */
    @PostMapping("/saveSettings")
    public SettingsInfo saveSettings(@RequestBody SettingsInfo settingsInfo) {
        settingsInfoService.insert(settingsInfo);
        return settingsInfo;
    }

    /**
     * 加载配置信息
     *
     * @param linkId the linkId
     * @return string
     */
    @GetMapping("/loadSettings/{linkId}")
    public List<SettingsInfo> loadSettings(@PathVariable("linkId") String linkId) {
        SettingsInfo query = new SettingsInfo();
        query.setLinkId(Long.parseLong(linkId));
        return settingsInfoService.selectList(new EntityWrapper<>(query));
    }

    /**
     * 加载配置信息详情
     *
     * @param id the id
     * @return string
     */
    @GetMapping("/loadSettingDetail/{id}")
    public Settings loadSettingDetail(String id) {
        SettingsInfo settingsInfo = settingsInfoService.selectById(Long.parseLong(id));
        return settingsInfo.getSettings();

    }

}
