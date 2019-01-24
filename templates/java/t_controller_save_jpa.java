
    /**
     * 新增或修改
     */
    @PostMapping("/save")
    [swaggerAnnotation]
    public [returnType] save(@RequestBody [entityClass] data){
        [service].save(data);

        return [returnInfo];
    }
