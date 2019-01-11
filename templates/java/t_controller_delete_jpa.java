
    /**
     * 删除
     */
    @[annotation]("/delete")
    public [returnType] delete(@RequestBody [primaryKeyType] id){
        [service].delete(id);

        return [returnInfo];
    }
