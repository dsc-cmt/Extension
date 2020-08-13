package com.cmt.extension.provider.config;


import com.cmt.extension.core.bootstrap.SpiProviderBootStrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FD
 * @date 2019-10-22
 **/

@Configuration
public class BootstrapConfig {

    @Bean
    public  SpiProviderBootStrap businessBootStrap() {
        return SpiProviderBootStrap.create();
    }
}
