package com.cmt.extension.consumer.config;

import com.cmt.extension.core.BusinessContext;
import com.cmt.extension.core.bootstrap.SpiConsumerBootStrap;
import com.cmt.extension.core.utils.ApplicationContextHolder;
import com.cmt.extension.consumer.service.TestService;
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
        SpiConsumerBootStrap spiBootStrap = new SpiConsumerBootStrap();
        spiBootStrap.setAppName("test");
        return spiBootStrap;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            public void run(ApplicationArguments args) throws Exception {
                TestService service = (TestService) ApplicationContextHolder.getApplicationContext().getBean("testService");
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
            }
        };
    }
}
