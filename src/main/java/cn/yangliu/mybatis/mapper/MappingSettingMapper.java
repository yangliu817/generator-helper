package cn.yangliu.mybatis.mapper;

import cn.yangliu.mybatis.bean.MappingSetting;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * The interface Mapping setting mapper.
 */
public interface MappingSettingMapper extends BaseMapper<MappingSetting> {

    /**
     * Load mapping list.
     *
     * @param queryPojo the query pojo
     * @return the list
     */
    List<MappingSetting> loadMapping(MappingSetting queryPojo);

    /**
     * Bathch insert.
     *
     * @param dataList the data list
     */
    void bathchInsert(List<MappingSetting> dataList);

}
