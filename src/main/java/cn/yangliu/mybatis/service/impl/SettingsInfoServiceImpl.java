package cn.yangliu.mybatis.service.impl;

import cn.yangliu.mybatis.bean.*;
import cn.yangliu.mybatis.mapper.*;
import cn.yangliu.mybatis.service.SettingsInfoService;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service
@Transactional
public class SettingsInfoServiceImpl extends ServiceImpl<SettingsInfoMapper, SettingsInfo> implements SettingsInfoService {

    @Autowired
    private ProjectSettingMapper projectSettingMapper;
    @Autowired
    private EntitySettingMapper entitySettingMapper;
    @Autowired
    private MapperSettingMapper mapperSettingMapper;
    @Autowired
    private ServiceSettingMapper serviceSettingMapper;
    @Autowired
    private ControllerSettingMapper controllerSettingMapper;
    @Autowired
    private SqliteSequenceMapper sqliteSequenceMapper;
    @Autowired
    private MappingSettingMapper mappingSettingMapper;

    @Override
    public boolean insert(SettingsInfo entity) {

        boolean flag = super.insert(entity);

        SqliteSequence sqliteSequence = sqliteSequenceMapper.selectOne(new SqliteSequence(entity));
        Long settingId = sqliteSequence.getSeq();

        Settings settings = entity.getSettings();
        ProjectSetting projectSetting = settings.getProject();
        projectSetting.setSettingId(settingId);

        EntitySetting entitySetting = settings.getEntity();
        entitySetting.setSettingId(settingId);

        MapperSetting mapperSetting = settings.getMapper();
        mapperSetting.setSettingId(settingId);

        ServiceSetting serviceSetting = settings.getService();
        serviceSetting.setSettingId(settingId);

        ControllerSetting controllerSetting = settings.getController();
        controllerSetting.setSettingId(settingId);

        List<MappingSetting> mapping = EntitySetting.getMappingList(entitySetting, settingId, entity.getDbType());
        if (mapping != null && mapping.size() > 0) {
            mappingSettingMapper.bathchInsert(mapping);
        }
        projectSettingMapper.insert(projectSetting);
        entitySettingMapper.insert(entitySetting);
        mapperSettingMapper.insert(mapperSetting);
        serviceSettingMapper.insert(serviceSetting);
        controllerSettingMapper.insert(controllerSetting);
        return flag;
    }

    @Override
    public List<SettingsInfo> selectList(Wrapper<SettingsInfo> wrapper) {
        List<SettingsInfo> list = super.selectList(wrapper);
        for (SettingsInfo settingsInfo : list) {
            querySettings(settingsInfo);
        }
        return list;
    }

    @Override
    public boolean deleteById(Serializable id) {
        boolean flag = super.deleteById(id);
        return flag;
    }

    private void querySettings(SettingsInfo settingsInfo) {
        if (settingsInfo == null) {
            throw new NullPointerException();
        }
        ProjectSetting projectSetting = projectSettingMapper.selectOne(new ProjectSetting(settingsInfo.getId()));
        EntitySetting entitySetting = entitySettingMapper.selectOne(new EntitySetting(settingsInfo.getId()));
        MapperSetting mapperSetting = mapperSettingMapper.selectOne(new MapperSetting(settingsInfo.getId()));
        ServiceSetting serviceSetting = serviceSettingMapper.selectOne(new ServiceSetting(settingsInfo.getId()));
        ControllerSetting controllerSetting = controllerSettingMapper.selectOne(new ControllerSetting(settingsInfo.getId()));

     /*   List<MappingSetting> mappingSettings = mappingSettingMapper.loadMapping(new MappingSetting(settingsInfo.getId(),null));

        entitySetting.setMapping(mappingSettings);*/
        Settings settings = new Settings(projectSetting, entitySetting, mapperSetting, serviceSetting, controllerSetting);

        settingsInfo.setSettings(settings);
    }

    @Override
    public SettingsInfo selectById(Serializable id) {
        SettingsInfo settingsInfo = super.selectById(id);
        querySettings(settingsInfo);
        return settingsInfo;
    }
}
