package cn.yangliu.mybatis.tools;

public class SystemUtils {

    public static void closeSystem(){
//        ApplicationConstans.POOL.shutdownNow();
        System.exit(0);
    }
}
