package cn.yangliu.mybatis.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Settings {

    private ProjectSetting project;

    private EntitySetting entity;

    private MapperSetting mapper;

    private RepositorySetting repository;

    private ServiceSetting service;

    private ControllerSetting controller;

    public Settings(ProjectSetting project, EntitySetting entity, RepositorySetting repository, MapperSetting mapper, ServiceSetting service, ControllerSetting controller) {
        this.project = project;
        this.entity = entity;
        this.repository = repository;
        this.mapper = mapper;
        this.service = service;
        this.controller = controller;
    }
}
