package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

@Data
public class MapperSource extends CodeSource {


    private Boolean extendBaseMapper;

    private Boolean useMapperAnonntation;

    private EntitySource entitySource;


    public MapperSource(ProjectSetting projectSetting, MapperSetting mapperSetting, EntitySource entitySource) {
        super(projectSetting, mapperSetting.getMapperPackage(), projectSetting.getCodePath());
        init(mapperSetting, entitySource.shortName);
        this.entitySource = entitySource;
    }

    private void init(MapperSetting mapperSetting, String entityName) {
        String mapperSufix = mapperSetting.getMapperSufix();
        extendBaseMapper = mapperSetting.getExtendBaseMapper();
        useMapperAnonntation = mapperSetting.getUseMapperAnonntation();

        shortName = entityName + mapperSufix.trim();

        filename = shortName + ".java";
        if (StringUtils.isNotEmpty(fullPackage)) {
            classFullName = fullPackage + "." + shortName;
        } else {
            classFullName = shortName;
        }
    }
}
