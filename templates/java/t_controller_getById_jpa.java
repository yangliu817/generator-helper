
    /**
     * 查询单个
     */
    @GetMapping("/findById/{[primaryKey]}")
    public [returnType] findById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].findById([primaryKey]);

        return [returnInfo];
    }
