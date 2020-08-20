package io.github.cmt.extension.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author shengchaojie
 * @date 2019-10-21
 **/
@SpringBootApplication
@ImportResource("classpath:dubbo-consumer.xml")
@EnableWebMvc
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }



}
