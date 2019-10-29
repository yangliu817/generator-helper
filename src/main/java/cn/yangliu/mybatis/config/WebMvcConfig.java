package cn.yangliu.mybatis.config;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring mvc设置
 * 杨柳
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    private static final SerializerFeature[] SERIALIZERFEATURES = {SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNonStringKeyAsString};

    @Bean
    public FastJsonConfig fastJsonConfig() {
        //数据为空展示为空字符串
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SERIALIZERFEATURES);
        return fastJsonConfig;
    }


    /**
     * 使用阿里fastjson转换 不使用jackson
     *
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters(FastJsonConfig fastJsonConfig) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }


}