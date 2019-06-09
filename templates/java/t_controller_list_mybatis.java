
    /**
     * 列表查询
     */
    @GetMapping("/list")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] list([entityClass] queryPojo, int pageNum, int pageSize){

        Page<[entityClass]> data = PageHelper.startPage(pageNum, pageSize, true);
        [service].selectList(queryPojo);

        return [returnInfo];
    }
