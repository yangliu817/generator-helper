
    /**
     * 删除
     */
    @[annotation]("/deleteById")
    [swaggerAnnotation]
    [shiroAnnotation]
    public [returnType] deleteById(@RequestBody [primaryKeyType] id){
        [service].deleteById(id);

        return [returnInfo];
    }
