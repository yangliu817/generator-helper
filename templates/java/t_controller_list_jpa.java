
    /**
     * 列表查询
     */
    @GetMapping("/findAll")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] findAll([entityClass] queryPojo, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<[entityClass]> data = [service].findAll(queryPojo, pageRequest);

        return [returnInfo];
    }
