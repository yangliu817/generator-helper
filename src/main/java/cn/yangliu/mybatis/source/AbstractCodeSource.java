package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public abstract class AbstractCodeSource extends AbstractSource implements Source {

    protected String fullPackage;

    protected String filepath;

    protected String filename;

    protected String classFullName;

    protected String shortName;

    protected Boolean useLombok;

    protected OrmTypeEnum ormType;

    protected String copyright;

    protected String author;

    protected String date;

    protected Boolean useSwagger;

    protected ProjectSetting projectSetting;

    public AbstractCodeSource(ProjectSetting projectSetting, String sourcePackage, String srcPath) {
        initBaseInfo(projectSetting.getProjectPackage(), sourcePackage, srcPath);
        useSwagger = projectSetting.getUseSwagger();
        useLombok = projectSetting.getUseLombok();
        ormType = OrmTypeEnum.getOrmTypeEnumByType(projectSetting.getOrmType());
        this.copyright = projectSetting.getCopyright();
        this.projectSetting = projectSetting;
        this.author = projectSetting.getAuthor();
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
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
