package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import lombok.Data;

import java.io.File;

@Data
public abstract class CodeSource extends AbstractSource implements Source {

    protected String fullPackage;

    protected String filepath;

    protected String filename;

    protected String classFullName;

    protected String shortName;

    protected Boolean useLombok;

    protected boolean mybatisPlus;

    public CodeSource(ProjectSetting projectSetting, String sourcePackage, String srcPath) {
        initBaseInfo(projectSetting.getProjectPackage(), sourcePackage, srcPath);
        useLombok = projectSetting.getUseLombok();
        mybatisPlus = (projectSetting.getMybatisType() == 2);
    }

    protected void initBaseInfo(String basePackage, String sourcePackage, String srcPath) {
        fullPackage = basePackage;
        if (StringUtils.isNotEmpty(sourcePackage)) {
            fullPackage += ApplicationContant.PACKAGE_SEPARATOR + sourcePackage;
        }

        if (srcPath.endsWith("/") || srcPath.endsWith("\\")) {
            filepath = srcPath + "src" + File.separator + "main" + File.separator + "java" + File.separator + fullPackage;
        } else {
            filepath = srcPath + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + fullPackage;
        }

        filepath = filepath.replace(ApplicationContant.PACKAGE_SEPARATOR, File.separator);
    }


}
