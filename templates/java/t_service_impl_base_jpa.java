[copyright]
[package]

[baseServiceImport]
import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author [author]
 * @date [date]
 */
public abstract class JpaServiceImpl<T, ID extends Serializable, Repository extends BaseRepository<T, ID>> implements JpaService<T, ID> {

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

    @Override
    public Optional<T> findOne(Specification<T> spec) {
        return repository.findOne(spec);
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return repository.findAll(spec);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository.findAll(spec,pageable);
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return repository.findAll(spec,sort);
    }

    @Override
    public long count(Specification<T> spec) {
        return repository.count(spec);
    }
	
	@Override
    public Specification<T> buildSpecification(Object queryPojo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (queryPojo != null) {

                List<Field> fieldList = getDeclaredFields(queryPojo);

                for (Field field : fieldList) {
                    FieldInfo fieldInfo = getFieldInfo(field, queryPojo);
                    if (Objects.isNull(fieldInfo)) {
                        continue;
                    }

                    String fieldName = fieldInfo.name;
                    Object fieldValue = fieldInfo.value;
                    if (Objects.isNull(fieldValue)) {
                        continue;
                    }
                    Path<?> path = root.get(fieldName);

                    Predicate predicate = cb.equal(path, fieldValue);
                    predicates.add(predicate);
                }

            }
            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }

    @Override
    public T getOneByJpql(T entity) {
        return (T) buildJpqlQuery(entity, entity.getClass()).getSingleResult();
    }

    @Override
    public List<T> listByJpql(T entity) {
        return (List<T>) buildJpqlQuery(entity, entity.getClass()).getResultList();
    }

    @Override
    public TypedQuery<?> buildJpqlQuery(Object queryPojo, Class<?> clazz) {
        StringBuilder sb = new StringBuilder("from ");
        sb.append(clazz.getSimpleName());

        List<Field> fieldList = getDeclaredFields(queryPojo);

        if (!fieldList.isEmpty()) {
            sb.append(" where ");
        }
      
        List<FieldInfo> fieldInfos = new ArrayList<>();
        for (Field field : fieldList) {
            FieldInfo fieldInfo = getFieldInfo(field, queryPojo);
            if (Objects.isNull(fieldInfo)) {
                continue;
            }
            fieldInfos.add(fieldInfo);
            String fieldName = fieldInfo.name;

            sb.append(" and ").append(fieldName).append(" = :").append(fieldName);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<?> query = entityManager.createQuery(sb.toString(), clazz);

        for (FieldInfo fieldInfo : fieldInfos) {
            Object fieldValue = fieldInfo.value;
            query.setParameter(fieldInfo.name, fieldValue);
        }

        return query;
    }

    private List<Field> getDeclaredFields(Object queryPojo) {
        Class<?> clazz = queryPojo.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (!clazz.equals(Object.class)) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length > 0) {
                fieldList.addAll(Arrays.asList(fields));
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    private static class FieldInfo {
        private String name;
        private Object value;

        FieldInfo(String name, Object value) {
            this.name = name;
            this.value = value;
        }

    }

    private FieldInfo getFieldInfo(Field field, Object queryPojo) {
        if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
            return null;
        }
        String fieldName = field.getName();
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        Object value = null;
        try {
            value = field.get(queryPojo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        if (!accessible) {
            field.setAccessible(false);
        }
        if (Objects.isNull(value)) {
            return null;
        }

        return new FieldInfo(fieldName, value);
    }
}
