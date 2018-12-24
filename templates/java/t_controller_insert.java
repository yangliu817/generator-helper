
    /**
     * 新增
     */
    @PostMapping("/insert")
    public [returnType] insert(@RequestBody [entityClass] data){
        [service].insert(data);

        return [returnInfo];
    }
