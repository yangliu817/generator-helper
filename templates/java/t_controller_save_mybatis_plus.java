
    /**
     * 新增
     */
    @PostMapping("/save")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] save(@RequestBody [entityClass] data){
        [service].save(data);

        return [returnInfo];
    }
