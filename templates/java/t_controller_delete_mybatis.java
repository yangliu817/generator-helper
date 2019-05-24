
    /**
     * 删除
     */
    @[annotation]("/delete")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        int result = [service].deleteBatchIds(Arrays.asList(ids));

        return [returnInfo];
    }
