
    /**
     * 新增或修改
     */
    @PostMapping("/save")
    public [returnType] save(@RequestBody [entityClass] data){
        [service].save(data);

        return [returnInfo];
    }
