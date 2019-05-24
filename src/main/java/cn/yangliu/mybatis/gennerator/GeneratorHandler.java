package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import cn.yangliu.mybatis.source.*;
import cn.yangliu.mybatis.tools.FileUtils;
import cn.yangliu.mybatis.tools.PathUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * @author mechrevo
 */
@Component
@Slf4j
public class GeneratorHandler {

    @Autowired
    private EntityGenerator entityGenerator;
    @Autowired
    private MapperGenerator mapperGenerator;
    @Autowired
    private ServiceImplGenerator serviceImplGenerator;
    @Autowired
    private ControllerGenerator controllerGenerator;
    @Autowired
    private XmlGenerator xmlGenerator;
    @Autowired
    private RepositoryGenerator repositoryGenerator;

    @Async
    public Future<Boolean> doGenerator(ProjectSetting projectSetting, EntitySource entitySource,
                                       RepositorySource repositorySource, MapperSource mapperSource, XmlSource xmlSource,
                                       ServiceImplSource serviceImplSource, ControllerSource controllerSource) {

        entityGenerator.generate(entitySource);

        if (Objects.equals(OrmTypeEnum.getOrmTypeEnumByType(projectSetting.getOrmType()), OrmTypeEnum.JPA)) {
            repositoryGenerator.generate(repositorySource);
        } else {
            mapperGenerator.generate(mapperSource);
            xmlGenerator.generate(xmlSource);
        }
        if (projectSetting.getCreateService()) {
            serviceImplGenerator.generate(serviceImplSource);
        }
        if (projectSetting.getCreateController() && projectSetting.getCreateService()) {
            controllerGenerator.generate(controllerSource);
        }
        return new AsyncResult<>(true);

    }

}
