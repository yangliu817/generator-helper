
    /**
     * 更新
     */
    @[annotation]("/update")
    [swaggerAnnotation]
    public [returnType] update(@RequestBody [entityClass] data){
        [service].updateById(data);

        return [returnInfo];
    }
