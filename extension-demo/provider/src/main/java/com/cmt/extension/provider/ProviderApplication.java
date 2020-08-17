package com.cmt.extension.provider;

import io.github.cmt.extension.provider.annotation.EnableExtensionProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@SpringBootApplication
@ImportResource(locations = {"classpath:application-dubbo.xml"})
@EnableWebMvc
@EnableExtensionProvider
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
