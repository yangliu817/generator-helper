package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

/**
 * The type Mapper source.
 */
@Data
public class MapperSource extends AbstractCodeSource {


    private Boolean extendBaseMapper;

    private Boolean useMapperAnonntation;

    private EntitySource entitySource;


    /**
     * Instantiates a new Mapper source.
     *
     * @param projectSetting the project setting
     * @param mapperSetting  the mapper setting
     * @param entitySource   the entity source
     */
    public MapperSource(ProjectSetting projectSetting, MapperSetting mapperSetting, EntitySource entitySource) {
        super(projectSetting, mapperSetting.getMapperPackage(), projectSetting.getCodePath());
        this.entitySource = entitySource;
        init(mapperSetting, entitySource.shortName.replace(entitySource.getClassSufix(),""));
    }

    private void init(MapperSetting mapperSetting, String entityName) {
        setPrimaryKeyInfo(entitySource);
        String mapperSufix = mapperSetting.getMapperSufix();
        extendBaseMapper = mapperSetting.getExtendBaseMapper();
        useMapperAnonntation = mapperSetting.getUseMapperAnonntation();

        shortName = entityName + mapperSufix.trim();

        filename = shortName + ".java";
        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + ApplicationContant.PACKAGE_SEPARATOR + shortName;
        } else {
            classFullName = shortName;
        }
    }
}
