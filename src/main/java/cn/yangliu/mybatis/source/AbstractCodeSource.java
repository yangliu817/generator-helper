package cn.yangliu.mybatis.source;

import cn.yangliu.comm.tools.StringUtils;
import cn.yangliu.mybatis.ApplicationContant;
import cn.yangliu.mybatis.bean.ProjectSetting;
import cn.yangliu.mybatis.enums.OrmTypeEnum;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The type Abstract code source.
 */
@Data
public abstract class AbstractCodeSource extends AbstractSource implements Source {

    /**
     * The Full package.
     */
    protected String fullPackage;

    /**
     * The Filepath.
     */
    protected String filepath;

    /**
     * The Filename.
     */
    protected String filename;

    /**
     * The Class full name.
     */
    protected String classFullName;

    /**
     * The Short name.
     */
    protected String shortName;

    /**
     * The Use lombok.
     */
    protected Boolean useLombok;

    /**
     * The Orm type.
     */
    protected OrmTypeEnum ormType;

    /**
     * The Copyright.
     */
    protected String copyright;

    /**
     * The Author.
     */
    protected String author;

    /**
     * The Date.
     */
    protected String date;

    /**
     * The Use swagger.
     */
    protected Boolean useSwagger;

    /**
     * The Project setting.
     */
    protected ProjectSetting projectSetting;

    /**
     * Instantiates a new Abstract code source.
     *
     * @param projectSetting the project setting
     * @param sourcePackage  the source package
     * @param srcPath        the src path
     */
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

    /**
     * Init base info.
     *
     * @param basePackage   the base package
     * @param sourcePackage the source package
     * @param srcPath       the src path
     */
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
