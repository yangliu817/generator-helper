
    /**
     * 删除
     */
    @[annotation]("/delete")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        [service].deleteBatchIds(Arrays.asList(ids));

        return [returnInfo];
    }
