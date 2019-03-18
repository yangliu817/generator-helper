
    /**
     * 新增
     */
    @PostMapping("/insert")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] insert(@RequestBody [entityClass] data){
        [service].insert(data);

        return [returnInfo];
    }
