package cn.yangliu.mybatis.source;

import cn.yangliu.mybatis.bean.MapperSetting;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

import java.io.File;

@Data
public class XmlSource implements Source {

    private String filename;

    private String filepath;

    private String mapperFullName;

    public XmlSource(ProjectSetting projectSetting, MapperSetting mapperSetting,MapperSource mapperSource , String entityName) {
        filepath = projectSetting.getCodePath();
        if (filepath.endsWith(File.separator)) {
            filepath += "src/main/resources"+File.separator+"mybatis";
        } else {
            filepath += File.separator+"src/main/resources"+File.separator + "mybatis";
        }
        filename = entityName + mapperSetting.getMapperSufix().trim() + ".xml";

        mapperFullName = mapperSource.getClassFullName();
    }

}
