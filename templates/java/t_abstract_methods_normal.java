    /** 全字段新增 */
    int insert([entityClass] entity);

    /** 有则新增 */
    int insertSelect([entityClass] entity);

    /** 批量插入 */
    int insertList(List<[entityClass]> list);

    /** 根据实体属性查询一条记录 */
    [entityClass] selectOne([entityClass] entity);

    /** 根据实体属性查询列表 */
    List<[entityClass]> selectList([entityClass] entity);

