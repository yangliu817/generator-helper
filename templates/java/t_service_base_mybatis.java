[copyright]
[package]

import org.springframework.transaction.TransactionStatus;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author [author]
 * @date [date]
 */
public interface MybatisService<T,ID extends Serializable> {

    T execute(Function<TransactionStatus, T> function);

    /** 全字段新增 */
    int insert(T entity);

    /** 有则新增 */
    int insertSelect(T entity);

    /** 批量插入 */
    int insertList(List<T> list);

    /** 根据实体属性查询一条记录 */
    T selectOne(T entity);

    /** 根据实体属性查询列表 */
    List<T> selectList(T entity);

    /** 根据id全字段修改 */
    int updateById(T entity);

    /** 根据id修改有值字段 */
    int updateSelectById(T entity);

    /** 根据id删除一条记录 */
    int deleteById(ID id);

    /** 根据id删除多条记录 */
    int deleteBatchIds(List<ID> ids);

    /** 根据id查询一条记录 */
    T selectById(ID id);
}
