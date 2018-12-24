package cn.yangliu.mybatis.service.impl;

import cn.yangliu.mybatis.bean.LinkInfo;
import cn.yangliu.mybatis.bean.SqliteSequence;
import cn.yangliu.mybatis.mapper.LinkInfoMapper;
import cn.yangliu.mybatis.mapper.SqliteSequenceMapper;
import cn.yangliu.mybatis.service.LinkInfoService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class LinkInfoServiceImpl extends ServiceImpl<LinkInfoMapper, LinkInfo> implements LinkInfoService {

    @Autowired
    private SqliteSequenceMapper sqliteSequenceMapper;

    @Override
    public List<LinkInfo> queryAll() {
        return baseMapper.queryAll();
    }

    @Override
    public synchronized boolean insert(LinkInfo entity) {
        boolean flag =  super.insert(entity);

        SqliteSequence sqliteSequence = sqliteSequenceMapper.selectOne(new SqliteSequence(entity));

        entity.setId(sqliteSequence.getSeq());
        return flag;
    }
}
