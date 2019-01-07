package cn.yangliu.mybatis.controller;

import cn.yangliu.mybatis.bean.Settings;
import cn.yangliu.mybatis.bean.SettingsInfo;
import cn.yangliu.mybatis.service.SettingsInfoService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.annotations.MappingController;
import de.felixroske.jfxsupport.web.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author 杨柳
 * @date 2019-01-06
 */
@MappingController
public class SettingController extends AbstractController {

    @Autowired
    private SettingsInfoService settingsInfoService;

    @Mapping("/saveSettings")
    public void saveSettings(String json) {
        SettingsInfo settingsInfo = JSON.parseObject(json, SettingsInfo.class);
        settingsInfoService.insert(settingsInfo);
    }

    @Mapping("/loadSettings")
    public String loadSettings(String id) {
        Long linkId = Long.parseLong(id);
        SettingsInfo query = new SettingsInfo();
        query.setLinkId(linkId);
        List<SettingsInfo> settingsInfos = settingsInfoService.selectList(new EntityWrapper<>(query));
        return JSON.toJSONString(settingsInfos);
    }

    @Mapping("/loadSettingDetail")
    public String loadSettingDetail(String id) {
        SettingsInfo settingsInfo = settingsInfoService.selectById(Long.parseLong(id));
        Settings settings = settingsInfo.getSettings();
        return JSON.toJSONString(settings);

    }

}
