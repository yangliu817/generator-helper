package cn.yangliu.mybatis.service;

import cn.yangliu.mybatis.bean.LinkInfo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface LinkInfoService extends IService<LinkInfo> {

    List<LinkInfo> queryAll();

}
