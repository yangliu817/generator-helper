package cn.yangliu.mybatis.config;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.yangliu.mybatis.mapper")
public class MybatisConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public PooledDataSource dataSource(){
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(driver);
        return dataSource;
    }
}
