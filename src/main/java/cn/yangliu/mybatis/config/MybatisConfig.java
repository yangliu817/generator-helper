package cn.yangliu.mybatis.config;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The type Mybatis config.
 */
@Configuration
@MapperScan("cn.yangliu.mybatis.mapper")
public class MybatisConfig {

    @Value("${datasource.driver-class-name}")
    private String driver;
    @Value("${datasource.url}")
    private String url;


    /**
     * Data source log 4 jdbc proxy data source.
     *
     * @return the log 4 jdbc proxy data source
     */
    @Bean
    @Primary
    public Log4jdbcProxyDataSource dataSource(){
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(driver);
        dataSource.setUrl(url);
        return new Log4jdbcProxyDataSource(dataSource);
    }
}
