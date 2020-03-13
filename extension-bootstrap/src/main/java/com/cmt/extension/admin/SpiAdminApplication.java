package com.cmt.extension.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @author tuzhenxian
 * @date 19-10-9
 */

@SpringBootApplication
public class SpiAdminApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpiAdminApplication.class, args);
    }

}
