
    /**
     * 列表
     */
    @GetMapping("/list")
    [swaggerAnnotation]
    public [returnType] list(@RequestParam [entityClass] queryPojo, int pageNum, int pageSize){

        Page<[entityClass]> data = PageHelper.startPage(pageNum, pageSize, true);
        [service].selectList(queryPojo);

        return [returnInfo];
    }
