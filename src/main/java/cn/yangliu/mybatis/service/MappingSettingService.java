package cn.yangliu.mybatis.service;

import cn.yangliu.mybatis.bean.MappingSetting;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface MappingSettingService extends IService<MappingSetting> {

    List<MappingSetting> loadMapping(MappingSetting queryPojo);

}
