
    /**
     * 查询单个
     */
    @GetMapping("/findById/{[primaryKey]}")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] findById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].findById([primaryKey]).orElse(null);

        return [returnInfo];
    }
