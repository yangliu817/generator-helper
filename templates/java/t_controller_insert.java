
    /**
     * 新增
     */
    @PostMapping("/insert")
    [swaggerAnnotation]
    public [returnType] insert(@RequestBody [entityClass] data){
        [service].insert(data);

        return [returnInfo];
    }
