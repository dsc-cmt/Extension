package com.cmt.extension.consumer.config;

import com.cmt.extension.consumer.service.TestService;
import com.cmt.extension.core.ExtensionHelper;
import com.cmt.extension.core.annotation.EnableExtensionConsumer;
import io.github.cmt.extension.common.ConfigMode;
import io.github.cmt.extension.common.BusinessContext;
import io.github.cmt.extension.common.util.ApplicationContextHolder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shengchaojie
 * @date 2019-10-21
 **/
@Configuration
@EnableExtensionConsumer(appName = "test",configMode = ConfigMode.LOCAL )
public class SpiConfiguration {

//    @Bean
//    public SpiConsumerBootStrap spiBootStrap() {
//        return SpiConsumerBootStrap.create()
//                .appName("test")
//                .configMode(ConfigMode.LOCAL);
////                .configMode(ConfigMode.REMOTE);
//    }

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

                ExtensionHelper.execute("f",()->{
                    String rst =  service.hello();
                    System.out.println(rst);
                    return rst;
                });

                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(()-> {
                    ExtensionHelper.execute("b",()->{
                        String rst =  service.hello();
                        System.out.println(rst);
                        return rst;
                    });
                },5,5, TimeUnit.SECONDS);
            }
        };
    }
}
