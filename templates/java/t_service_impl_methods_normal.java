
    /** 全字段新增 */
    @Override
    public int insert([entityClass] entity) {
        return service.insert(entity);
    }

    /** 有则新增 */
    @Override
    public int insertSelect([entityClass] entity) {
        return [mapperName].insertSelect(entity);
    }

    /** 批量插入 */
    @Override
    public int insertList(List<[entityClass]> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return [mapperName].insertList(list);
    }

    /** 根据实体属性查询一条记录 */
    @Override
    public [entityClass] selectOne([entityClass] entity) {
        return [mapperName].selectOne(id);
    }

    /** 根据实体属性查询列表 */
    @Override
    public List<[entityClass]> selectList([entityClass] entity) {
        return [mapperName].selectList(entity);
    }
