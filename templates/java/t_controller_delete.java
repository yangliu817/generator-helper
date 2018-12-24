
    /**
     * 删除
     */
    @[annotation]("/delete")
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        [service].deleteBatchIds(Arrays.asList(ids));

        return [returnInfo];
    }
