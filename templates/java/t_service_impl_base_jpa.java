[package]

[baseServiceImport]
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author [author]
 * @contact [contact]
 * @date [date]
 */
public abstract class JpaServiceImpl<T, ID, Repository extends JpaRepository<T, ID>> implements JpaService<T, ID> {

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    @Autowired
    protected Repository repository;

    @Autowired
    protected TransactionTemplate transactionTemplate;

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
    public List<T> findAll() {

        return repository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllInBatch() {
        repository.deleteAllInBatch();
    }

    @Override
    public T getOne(ID id) {
        return repository.getOne(id);
    }

    @Override
    public Optional<T> findOne(T queryPojo) {
        return repository.findOne(Example.of(queryPojo));
    }

    @Override
    public Iterable<T> findAll(T queryPojo) {
        return repository.findAll(Example.of(queryPojo));
    }

    @Override
    public Iterable<T> findAll(T queryPojo, Sort sort) {
        return repository.findAll(Example.of(queryPojo), sort);
    }

    @Override
    public Page<T> findAll(T queryPojo, Pageable pageable) {
        return repository.findAll(Example.of(queryPojo), pageable);
    }

    @Override
    public long count(T queryPojo) {
        return repository.count(Example.of(queryPojo));
    }

    @Override
    public boolean exists(T queryPojo) {
        return repository.exists(Example.of(queryPojo));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInBatch(Iterable<T> entities) {
        repository.deleteInBatch(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T saveAndFlush(T entity) {
        return repository.saveAndFlush(entity);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Iterable<? extends T> iterable) {
        repository.deleteAll(iterable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }
}
