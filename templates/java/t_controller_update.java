
    /**
     * 更新
     */
    @[annotation]("/update")
    public [returnType] update(@RequestBody [entityClass] data){
        [service].updateById(data);

        return [returnInfo];
    }
