package cn.yangliu.mybatis.controller;

import java.io.File;

import cn.yangliu.mybatis.anonntations.JsonResponse;
import cn.yangliu.mybatis.ex.HelperException;
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


    /**
     * Load environment info string.
     *
     * @return the string
     */
    @GetMapping("/isWindows")
    public boolean isWindows() {
        String osName = System.getProperty("os.name").toUpperCase();
        return osName.contains("WINDOWS");
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
            throw new HelperException("目录不存在");
        }
        if (!isWindows()){
            throw new HelperException("仅windows可用");
        }
        Runtime.getRuntime().exec("cmd /c start explorer " + codePath);
    }
}
