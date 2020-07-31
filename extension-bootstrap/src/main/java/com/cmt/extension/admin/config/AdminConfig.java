package com.cmt.extension.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * bean 配置类
 *
 * @author tuzhenxian
 * @date 19-10-9
 */
@Configuration
public class AdminConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("index.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("css/**").addResourceLocations("classpath:/META-INF/resources/css/");
        registry.addResourceHandler("img/**").addResourceLocations("classpath:/META-INF/resources/img/");
        registry.addResourceHandler("js/**").addResourceLocations("classpath:/META-INF/resources/js/");
    }
}
