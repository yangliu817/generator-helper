package cn.yangliu.mybatis.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 作者 杨柳
 * 时间 2017-09-26 01:13
 * 描述
 */
@Slf4j
public final class PathUtils {

    private static String path;

    /**
     * Gets home path.
     *
     * @param clazz the clazz
     * @return the home path
     */
    public final static String getHomePath(Class<?> clazz) {

        if (path == null) {
            synchronized (PathUtils.class) {
                if (path == null) {
                    init(clazz);
                }
            }
        }

        return path;

    }

    private static void init(Class<?> clazz) {
        String path;
        try {
            URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
            String jarPath = url.getPath();
            String classLocation = URLDecoder.decode(jarPath, "utf-8");
            if (classLocation.endsWith(".jar")) {
                classLocation = classLocation.substring(0, classLocation.lastIndexOf("/") + 1);
            }
            if (classLocation.endsWith("/")) {
                classLocation = classLocation.substring(0, classLocation.lastIndexOf("/"));
            }
            path = classLocation.substring(0, classLocation.lastIndexOf("/"));
            if (path.endsWith("target")) {
                path = path.replace("target", "");
            }
            String os = System.getProperty("os.name");
            os = os.toLowerCase();
            if (os.startsWith("windows") && path.startsWith("/")) {
                path = path.substring(1);
            }

            if (path.endsWith("BOOT-INF")) {
                File file = new File(path);
                path = file.getParentFile().getParentFile().getParent();
            }
            if (path.startsWith("file:"+File.separator)){
                path = path.replace("file:"+File.separator,"");
            }
            PathUtils.path = path;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
