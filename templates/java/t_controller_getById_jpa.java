
    /**
     * 查询单个
     */
    @GetMapping("/findById/{[primaryKey]}")
    [swaggerAnnotation]
    public [returnType] findById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].findById([primaryKey]);

        return [returnInfo];
    }
