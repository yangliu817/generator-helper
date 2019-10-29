package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

import java.io.File;

/**
 * The type Xml source.
 */
@Data
public class XmlSource extends AbstractSource {

    private String filename;

    private String filepath;

    private String mapperFullName;

    private boolean mybatisPlus;

    private EntitySource entitySource;

    private String dbType;

    /**
     * Instantiates a new Xml source.
     *
     * @param projectSetting the project setting
     * @param mapperSetting  the mapper setting
     * @param mapperSource   the mapper source
     * @param entityName     the entity name
     * @param dbType         the db type
     */
    public XmlSource(ProjectSetting projectSetting, MapperSetting mapperSetting, MapperSource mapperSource, String entityName,String dbType) {
        filepath = projectSetting.getCodePath();
        if (filepath.endsWith(File.separator)) {
            filepath += "src/main/resources" + File.separator + "mybatis";
        } else {
            filepath += File.separator + "src/main/resources" + File.separator + "mybatis";
        }

        this.entitySource = mapperSource.getEntitySource();

        this.dbType = dbType;

        filename = entitySource.shortName.replace(entitySource.getClassSufix(),"") + mapperSetting.getMapperSufix().trim() + ".xml";

        mapperFullName = mapperSource.getClassFullName();

        mybatisPlus = (projectSetting.getOrmType() == 2);

        setPrimaryKeyInfo(entitySource);
    }

}
