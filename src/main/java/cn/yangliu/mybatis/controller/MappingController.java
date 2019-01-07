package cn.yangliu.mybatis.controller;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.JavaType;
import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.service.JavaTypeService;
import cn.yangliu.mybatis.service.MappingSettingService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.web.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
