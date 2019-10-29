package cn.yangliu.mybatis.service;

import cn.yangliu.mybatis.bean.LinkInfo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * The interface Link info service.
 */
public interface LinkInfoService extends IService<LinkInfo> {

    /**
     * Query all list.
     *
     * @return the list
     */
    List<LinkInfo> queryAll();

}
