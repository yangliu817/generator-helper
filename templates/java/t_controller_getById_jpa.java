
    /**
     * 查询单个
     */
    @GetMapping("/getById/{[primaryKey]}")
    public [returnType] getById(@PathVariable("[primaryKey]") [primaryKeyType] [primaryKey]){
        [entityClass] data = [service].findOne([primaryKey]);

        return [returnInfo];
    }
