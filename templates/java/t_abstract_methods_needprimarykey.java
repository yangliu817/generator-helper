    /** 根据id全字段修改 */
    int updateById([entityClass] entity);

    /** 根据id修改有值字段 */
    int updateSelectById([entityClass] entity);

    /** 根据id删除一条记录 */
    int deleteById([primaryKeyType] id);

    /** 根据id删除多条记录 */
    int deleteByIds(List<[primaryKeyType]> ids);

    /** 根据id查询一条记录 */
    [entityClass] selectById([primaryKeyType] id);
