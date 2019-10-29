package cn.yangliu.mybatis;


import cn.yangliu.comm.config.CommConfig;
import cn.yangliu.mybatis.tools.DBUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * The type Generator helper application.
 */
@SpringBootApplication(exclude = CommConfig.class)
@Slf4j
@EnableAsync
public class GeneratorHelperApplication  {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        DBUtils.init();
      SpringApplication.run(GeneratorHelperApplication.class,args);
    }


}
