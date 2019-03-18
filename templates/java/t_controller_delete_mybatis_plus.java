
    /**
     * 删除
     */
    @[annotation]("/delete")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        [service].removeByIds(Arrays.asList(ids));

        return [returnInfo];
    }
