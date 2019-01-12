
    /** 根据id删除一条记录 */
    @Override
    public void deleteById([primaryKeyType] id) {
        [repositoryName].deleteById(id);
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
    public void saveAll(List<[entityClass]> list) {
        [repositoryName].saveAll(list);
    }

    /** 根据id查询 */
    @Override
    public [entityClass] findById([primaryKeyType] id) {
        Optional<[entityClass]> optional = userRepository.findById(id);
        [entityClass] [entityClass-l] = null;
        if (optional.isPresent()) {
            [entityClass-l] = optional.get();
        }
        return [entityClass-l];
    }

    /** 根据条件查询单个 */
    @Override
    public [entityClass] findOne([entityClass] query) {
        Specification<[entityClass]> specification = createSpecification(query);
        Optional<[entityClass]> optional = userRepository.findOne(specification);
        [entityClass] [entityClass-l] = null;
        if (optional.isPresent()) {
            [entityClass-l] = optional.get();
        }
        return [entityClass-l];
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