package cn.yangliu.mybatis.controller;

import de.felixroske.jfxsupport.annotations.Mapping;
import de.felixroske.jfxsupport.annotations.MappingController;
import de.felixroske.jfxsupport.web.AbstractController;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * @author 杨柳
 * @date 2019-01-09
 */
@MappingController
public class EnvironmentController extends AbstractController {

    @Value("${spring.profiles.active:prod}")
    private String environment;

    @Mapping("/loadEnvironmentInfo")
    public String loadEnvironmentInfo() {
        return environment;
    }

    @Mapping("/openCodeFolder")
    public void openCodeFolder(String codePath) {
        try {
            File file = new File(codePath);
            if (!file.exists()){
                file.mkdirs();
            }
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
