    /** 根据id全字段修改 */
    @Override
    public int updateById([entityClass] entity) {
        return [mapperName].updateById(entity);
    }

    /** 根据id修改有值字段 */
    @Override
    public int updateSelectById([entityClass] entity) {
        return [mapperName].updateSelectById(entity);
    }

    /** 根据id删除一条记录 */
    @Override
    public int deleteById([primaryKeyType] id) {
        return [mapperName].deleteById(id);
    }

    /** 根据id删除多条记录 */
    @Override
    public int deleteBatchIds(List<[primaryKeyType]> ids) {
        if (ids == null || ids.size() == 0) {
            return 0;
        }
        return [mapperName].deleteBatchIds(ids);
    }

    /** 根据id查询一条记录 */
    @Override
    public [entityClass] selectById([primaryKeyType] id) {
        return [mapperName].selectById(id);
    }
