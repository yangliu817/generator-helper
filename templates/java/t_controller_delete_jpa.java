
    /**
     * 删除
     */
    @[annotation]("/deleteById")
    public [returnType] deleteById(@RequestBody [primaryKeyType] id){
        [service].deleteById(id);

        return [returnInfo];
    }
