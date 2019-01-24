
    /**
     * 删除
     */
    @[annotation]("/deleteById")
    [swaggerAnnotation]
    public [returnType] deleteById(@RequestBody [primaryKeyType] id){
        [service].deleteById(id);

        return [returnInfo];
    }
