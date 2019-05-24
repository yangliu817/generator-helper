[copyright]
[package]

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author [author]
 * @date [date]
 */
public abstract class MybatisServiceImpl<T,ID extends Serializable,Mapper extends MybatisMapper<T,ID>> implements MybatisService<T,ID> {

    @Autowired
    protected TransactionTemplate transactionTemplate;

    @Autowired
    protected Mapper mapper;

    @Override
    public T execute(Function<TransactionStatus, T> function) {
        return transactionTemplate.execute(status -> {
            T result = null;
            try {
                result = function.apply(status);
            } catch (Exception e) {
                status.setRollbackOnly();
            }
            return result;
        });
    }

    @Override
    public int insert(T entity) {
        return mapper.insert(entity);
    }

    @Override
    public int insertSelect(T entity) {
        return mapper.insertSelect(entity);
    }

    @Override
    public int insertList(List<T> list) {
        return mapper.insertList(list);
    }

    @Override
    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Override
    public List<T> selectList(T entity) {
        return mapper.selectList(entity);
    }

    @Override
    public int updateById(T entity) {
        return mapper.updateById(entity);
    }

    @Override
    public int updateSelectById(T entity) {
        return mapper.updateSelectById(entity);
    }

    @Override
    public int deleteById(ID id) {
        return mapper.deleteById(id);
    }

    @Override
    public int deleteBatchIds(List<ID> ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public T selectById(ID id) {
        return mapper.selectById(id);
    }
}