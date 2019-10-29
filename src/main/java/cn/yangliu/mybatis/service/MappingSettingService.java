package cn.yangliu.mybatis.service;

import cn.yangliu.mybatis.bean.MappingSetting;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * The interface Mapping setting service.
 */
public interface MappingSettingService extends IService<MappingSetting> {

    /**
     * Load mapping list.
     *
     * @param queryPojo the query pojo
     * @return the list
     */
    List<MappingSetting> loadMapping(MappingSetting queryPojo);

}
