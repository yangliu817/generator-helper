package cn.yangliu.mybatis.service.impl;

import cn.yangliu.mybatis.bean.ColumnType;
import cn.yangliu.mybatis.mapper.ColumnTypeMapper;
import cn.yangliu.mybatis.service.ColumnTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * The type Column type service.
 */
@Service
public class ColumnTypeServiceImpl extends ServiceImpl<ColumnTypeMapper, ColumnType> implements ColumnTypeService {
}
