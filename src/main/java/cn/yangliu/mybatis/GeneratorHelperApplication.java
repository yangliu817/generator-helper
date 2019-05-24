package cn.yangliu.mybatis;


import cn.yangliu.comm.config.CommConfig;
import cn.yangliu.mybatis.tools.DBUtils;
import cn.yangliu.mybatis.tools.PathUtils;
import cn.yangliu.mybatis.tools.SystemUtils;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import de.felixroske.jfxsupport.GUIState;
import de.felixroske.jfxsupport.SplashScreen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication(exclude = CommConfig.class)
@Slf4j
@EnableAsync
public class GeneratorHelperApplication extends AbstractJavaFxApplicationSupport {

    static {
        SplashScreen.setImagePath("/imgs/start.png");
    }

    public static void main(String[] args) {
        DBUtils.init();
        launchApp(GeneratorHelperApplication.class, args);
//        SpringApplication.run(MybatisPlusHelperApplication.class,args);
    }

    @Override
    public void beforeInitialView() {
        super.beforeInitialView();
        Stage stage = GUIState.getStage();
        stage.setOnCloseRequest(event -> SystemUtils.closeSystem());
       /* stage.setMinWidth(1280);
        stage.setMaxWidth(1280);
        stage.setMinHeight(860);
        stage.setMaxHeight(860);*/
    }

    @Override
    protected String getUrl() {
        String url = super.getUrl();
        if (url.startsWith("file:")) {
            String path = PathUtils.getHomePath(GeneratorHelperApplication.class);

            if (!path.endsWith("/")) {
                path += "/";
            }
            url = url.replace("file:", "");

            url = "file:" + path + url;
        }
        return url;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        SystemUtils.closeSystem();
    }
}
