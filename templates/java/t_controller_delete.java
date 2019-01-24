
    /**
     * 删除
     */
    @[annotation]("/delete")
    [swaggerAnnotation]
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        [service].deleteBatchIds(Arrays.asList(ids));

        return [returnInfo];
    }
