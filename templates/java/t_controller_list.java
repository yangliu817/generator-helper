
    /**
     * 列表
     */
    @GetMapping("/list")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] list(@RequestParam [entityClass] queryPojo, Page<[entityClass]> page){
        Page<[entityClass]> data = [service].selectPage(page, new EntityWrapper<>(queryPojo));

        return [returnInfo];
    }
