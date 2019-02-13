
    /**
     * 更新
     */
    @[annotation]("/update")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] update(@RequestBody [entityClass] data){
        [service].updateById(data);

        return [returnInfo];
    }
