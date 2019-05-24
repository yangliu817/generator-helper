
    /**
     * 删除
     */
    @[annotation]("/delete")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] delete(@RequestBody [primaryKeyType][] ids){
        boolean result = [service].removeByIds(Arrays.asList(ids));

        return [returnInfo];
    }
