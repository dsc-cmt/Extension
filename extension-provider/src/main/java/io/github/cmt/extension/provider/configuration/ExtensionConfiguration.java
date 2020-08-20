package io.github.cmt.extension.provider.configuration;

import io.github.cmt.extension.common.util.ApplicationContextHolder;
import io.github.cmt.extension.provider.ExtensionAnnotationPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shengchaojie
 * @date 2020/8/16
 **/
@Configuration
public class ExtensionConfiguration {

    @Bean
    public ExtensionAnnotationPostProcessor extensionBeanPostProcessor(){
        return new ExtensionAnnotationPostProcessor();
    }

    @Bean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }

}
