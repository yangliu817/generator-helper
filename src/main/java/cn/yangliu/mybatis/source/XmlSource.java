package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

import java.io.File;

@Data
public class XmlSource extends AbstractSource {

    private String filename;

    private String filepath;

    private String mapperFullName;

    private boolean mybatisPlus;

    private EntitySource entitySource;

    public XmlSource(ProjectSetting projectSetting, MapperSetting mapperSetting, MapperSource mapperSource, String entityName) {
        filepath = projectSetting.getCodePath();
        if (filepath.endsWith(File.separator)) {
            filepath += "src/main/resources" + File.separator + "mybatis";
        } else {
            filepath += File.separator + "src/main/resources" + File.separator + "mybatis";
        }

        this.entitySource = mapperSource.getEntitySource();

        filename = entityName + mapperSetting.getMapperSufix().trim() + ".xml";

        mapperFullName = mapperSource.getClassFullName();

        mybatisPlus = (projectSetting.getMybatisType() == 2);

        setPrimaryKeyInfo(entitySource);
    }

}
