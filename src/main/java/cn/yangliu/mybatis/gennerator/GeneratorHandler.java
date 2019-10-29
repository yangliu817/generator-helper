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
 * The type Generator handler.
 *
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

    /**
     * Do generator future.
     *
     * @param projectSetting    the project setting
     * @param entitySource      the entity source
     * @param repositorySource  the repository source
     * @param mapperSource      the mapper source
     * @param xmlSource         the xml source
     * @param serviceImplSource the service impl source
     * @param controllerSource  the controller source
     * @return the future
     */
    @Async
    public Future<Boolean> doGenerator(ProjectSetting projectSetting, EntitySource entitySource,
                                       RepositorySource repositorySource, MapperSource mapperSource, XmlSource xmlSource,
                                       ServiceImplSource serviceImplSource, ControllerSource controllerSource) {

        //生成entity
        entityGenerator.generate(entitySource);

        //如果orm是jpa 生成repository
        if (Objects.equals(OrmTypeEnum.getOrmTypeEnumByType(projectSetting.getOrmType()), OrmTypeEnum.JPA)) {
            repositoryGenerator.generate(repositorySource);
        } else {
            //mybatis 或者 mybatis-plus 生成mapper 和xml
            mapperGenerator.generate(mapperSource);
            xmlGenerator.generate(xmlSource);
        }
        //生成 serviceimpl
        if (projectSetting.getCreateService()) {
            serviceImplGenerator.generate(serviceImplSource);
        }

        //生成controller
        if (projectSetting.getCreateController() && projectSetting.getCreateService()) {
            controllerGenerator.generate(controllerSource);
        }
        return new AsyncResult<>(true);

    }

}
