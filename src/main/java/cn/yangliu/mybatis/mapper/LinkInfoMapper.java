package cn.yangliu.mybatis.mapper;

import cn.yangliu.mybatis.bean.LinkInfo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface LinkInfoMapper extends BaseMapper<LinkInfo> {

    List<LinkInfo> queryAll();

}
