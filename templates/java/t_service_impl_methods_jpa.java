
    /** 根据id删除一条记录 */
    @Override
    public void delete([primaryKeyType] id) {
        [repositoryName].delete(id);
    }

    /** 批量删除 */
    @Override
    public void deleteInBatch(List<[entityClass]> list) {
        [repositoryName].deleteInBatch(list);
    }

    /** 保存(新增或者修改) */
    @Override
    public void save([entityClass] entity) {
        [repositoryName].save(entity);
    }

    /** 批量保存(新增或者修改) */
    @Override
    public void save(List<[entityClass]> list) {
        [repositoryName].save(list);
    }

    /** 根据id查询 */
    @Override
    public [entityClass] findOne([primaryKeyType] id) {
        return [repositoryName].findOne(id);
    }

    /** 根据条件查询单个 */
    @Override
    public [entityClass] findOne([entityClass] query) {
        Specification<[entityClass]> specification = createSpecification(query);
        return [repositoryName].findOne(specification);
    }

    /** 根据条件查询列表 */
    @Override
    public Page<[entityClass]> findAll([entityClass] query, Pageable pageable) {
        Specification<[entityClass]> specification = createSpecification(query);
        return [repositoryName].findAll(specification, pageable);
    }

    /** 创建查询条件 */
    private static Specification<[entityClass]> createSpecification(final [entityClass] query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

[if]
            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        };
    }