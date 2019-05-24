[copyright]
[package]

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.transaction.TransactionStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author [author]
 * @date [date]
 */
public interface JpaService<T, ID extends Serializable> {

    T execute(Function<TransactionStatus, T> function);

    List<T> findAll();

    List<T> findAll(Sort sort);

    Page<T> findAll(Pageable pageable);

    List<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> iterable);

    void deleteAll();

    T save(T entity);

    List<T> saveAll(Iterable<T> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    T saveAndFlush(T entity);

    void deleteInBatch(Iterable<T> entities);

    void deleteAllInBatch();

    T getOne(ID id);

    Optional<T> findOne(T queryPojo);

    Iterable<T> findAll(T queryPojo);

    Iterable<T> findAll(T queryPojo, Sort sort);

    Page<T> findAll(T queryPojo, Pageable pageable);

    long count(T queryPojo);

    boolean exists(T queryPojo);

    Optional<T> findOne(@Nullable Specification<T> spec);

    List<T> findAll(@Nullable Specification<T> spec);

    Page<T> findAll(@Nullable Specification<T> spec, Pageable pageable);

    List<T> findAll(@Nullable Specification<T> spec, Sort sort);

    long count(@Nullable Specification<T> spec);
}
