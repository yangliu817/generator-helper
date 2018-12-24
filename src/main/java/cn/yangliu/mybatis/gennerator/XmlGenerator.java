package cn.yangliu.mybatis.gennerator;

import cn.yangliu.mybatis.source.XmlSource;
import cn.yangliu.mybatis.tools.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class XmlGenerator implements Generator<XmlSource> {

    private String t_xml;

    public XmlGenerator() {
        t_xml = FileUtils.read(FileUtils.getFullPath("templates", "t_mapper.xml"),true);
    }

    @Override
    public void generate(XmlSource source) {
        String xmlCode = t_xml.replace("","");
        xmlCode = xmlCode.replace("[mapper-class-full-name]",source.getMapperFullName());
        FileUtils.output(xmlCode,source.getFilepath(),source.getFilename());
    }
}
