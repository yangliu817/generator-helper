
    /**
     * 列表查询
     */
    @GetMapping("/list")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] list([entityClass] queryPojo, Page<[entityClass]> page){
        IPage<[entityClass]> data = [service].page(page, new QueryWrapper<>(queryPojo));

        return [returnInfo];
    }
