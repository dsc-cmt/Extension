package com.cmt.extension.consumer.config;

import com.cmt.extension.core.BusinessContext;
import com.cmt.extension.core.bootstrap.SpiConsumerBootStrap;
import com.cmt.extension.core.common.ConfigMode;
import com.cmt.extension.core.utils.ApplicationContextHolder;
import com.cmt.extension.consumer.service.TestService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shengchaojie
 * @date 2019-10-21
 **/
@Configuration
public class SpiConfiguration {

    @Bean
    public SpiConsumerBootStrap spiBootStrap() {
        return SpiConsumerBootStrap.create()
                .appName("test")
                .configMode(ConfigMode.LOCAL);
//                .configMode(ConfigMode.REMOTE);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            public void run(ApplicationArguments args) throws Exception {
                final TestService service = (TestService) ApplicationContextHolder.getApplicationContext().getBean("testService");
                BusinessContext.setBizCode("a");
                String result = service.hello();
                System.out.println(result);
                BusinessContext.setBizCode("b");
                result = service.hello();
                System.out.println(result);
                BusinessContext.setBizCode("c");
                result = service.hello();
                System.out.println(result);
                BusinessContext.setBizCode("d");
                result = service.hello();
                System.out.println(result);
                BusinessContext.setBizCode("e");
                result = service.hello();
                System.out.println(result);
                BusinessContext.setBizCode("f");
                result = service.hello();
                System.out.println(result);

                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(()-> {
                    BusinessContext.setBizCode("b");
                    System.out.println(service.hello());
                },5,5, TimeUnit.SECONDS);
            }
        };
    }
}
