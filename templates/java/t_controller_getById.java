
    /**
     * 查询单个
     */
    @GetMapping("/getById/{[primaryKey]}")
    public [returnType] getById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].selectById([primaryKey]);

        return [returnInfo];
    }
