
    /** 根据id删除一条记录 */
    void deleteById([primaryKeyType] id);

    /** 批量删除 */
    void deleteInBatch(List<[entityClass]> list);

    /** 保存(新增或者修改) */
    void save([entityClass] entity);

    /** 批量保存(新增或者修改) */
    void saveAll(List<[entityClass]> list);

    /** 根据id查询 */
    [entityClass] findById([primaryKeyType] id);

    /** 根据条件查询单个 */
    [entityClass] findOne([entityClass] query);

    /** 根据条件查询列表 */
    Page<[entityClass]> findAll([entityClass] query, Pageable pageable);
