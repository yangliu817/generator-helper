package cn.yangliu.mybatis.mapper;

import cn.yangliu.mybatis.bean.LinkInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * The interface Link info mapper.
 */
public interface LinkInfoMapper extends BaseMapper<LinkInfo> {

    /**
     * 查询所有连接信息
     *
     * @return 所有连接信息 list
     */
    List<LinkInfo> queryAll();

}
