package cn.yangliu.mybatis.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Settings.
 */
@Data
@NoArgsConstructor
public class Settings {

    private ProjectSetting project;

    private EntitySetting entity;

    private MapperSetting mapper;

    private RepositorySetting repository;

    private ServiceSetting service;

    private ControllerSetting controller;

    /**
     * Instantiates a new Settings.
     *
     * @param project    the project
     * @param entity     the entity
     * @param repository the repository
     * @param mapper     the mapper
     * @param service    the service
     * @param controller the controller
     */
    public Settings(ProjectSetting project, EntitySetting entity, RepositorySetting repository, MapperSetting mapper, ServiceSetting service, ControllerSetting controller) {
        this.project = project;
        this.entity = entity;
        this.repository = repository;
        this.mapper = mapper;
        this.service = service;
        this.controller = controller;
    }
}
