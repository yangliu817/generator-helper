package cn.yangliu.mybatis;

import cn.yangliu.mybatis.tools.FileUtils;
import cn.yangliu.mybatis.tools.PathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class ApplicationContant {

    public static final String PACKAGE_SEPARATOR = ".";

    public static Properties config;

    static {
        init();
    }


    public static void init() {
        try {
            String path = PathUtils.getHomePath(ApplicationContant.class);
            File file = new File(path + File.separator + "config");
            AbstractResource resource;
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, "config.properties");
            if (!file.exists()) {
                String content = FileUtils.read(Resources.getResourceAsStream("config.properties"), true);
                FileUtils.output(content, file.getParent(), file.getName());
            }

            resource = new FileSystemResource(file.getAbsolutePath());

            config = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
