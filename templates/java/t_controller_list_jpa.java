
    /**
     * 列表
     */
    @GetMapping("/list")
    public [returnType] list(@RequestParam [entityClass] queryPojo, int page, int size){
        PageRequest pageRequest = new PageRequest(page, size);
        Page<[entityClass]> data = [service].findAll(queryPojo, pageRequest);

        return [returnInfo];
    }
