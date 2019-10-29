package cn.yangliu.mybatis.controller;

import java.io.File;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Environment controller.
 *
 * @author 杨柳
 * @date 2019 -01-09
 */
@RestController
@JsonResponse
public class EnvironmentController {

    @Value("${spring.profiles.active:prod}")
    private String environment;

    /**
     * Load environment info string.
     *
     * @return the string
     */
    @GetMapping("/loadEnvironmentInfo")
    public String loadEnvironmentInfo() {
        return environment;
    }

    /**
     * Open code folder.
     *
     * @param codePath the code path
     */
    @GetMapping("/openCodeFolder")
    public void openCodeFolder(String codePath) throws Exception {
        File file = new File(codePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        java.awt.Desktop.getDesktop().open(file);
    }
}
