package cn.yangliu.mybatis.service.impl;

import cn.yangliu.mybatis.bean.MappingSetting;
import cn.yangliu.mybatis.mapper.MappingSettingMapper;
import cn.yangliu.mybatis.service.MappingSettingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Mapping setting service.
 */
@Service
public class MappingSettingServiceImpl extends ServiceImpl<MappingSettingMapper, MappingSetting> implements MappingSettingService {

    @Override
    public List<MappingSetting> loadMapping(MappingSetting queryPojo) {
        return baseMapper.loadMapping(queryPojo);
    }
}
