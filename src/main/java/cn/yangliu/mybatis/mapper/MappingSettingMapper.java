package cn.yangliu.mybatis.mapper;

import cn.yangliu.mybatis.bean.MappingSetting;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface MappingSettingMapper extends BaseMapper<MappingSetting> {

    List<MappingSetting> loadMapping(MappingSetting queryPojo);

    void bathchInsert(List<MappingSetting> dataList);

}
