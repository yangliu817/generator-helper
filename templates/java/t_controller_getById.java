
    /**
     * 查询单个
     */
    @GetMapping("/getById/{[primaryKey]}")
    [swaggerAnnotation]
    public [returnType] getById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].selectById([primaryKey]);

        return [returnInfo];
    }
