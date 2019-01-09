package cn.yangliu.mybatis.config;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@MapperScan("cn.yangliu.mybatis.mapper")
public class MybatisConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public PooledDataSource pooledDataSource(){
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(driver);
        return dataSource;
    }

    @Bean
    @Primary
    public Log4jdbcProxyDataSource dataSource(){
        return new Log4jdbcProxyDataSource(pooledDataSource());
    }
}
