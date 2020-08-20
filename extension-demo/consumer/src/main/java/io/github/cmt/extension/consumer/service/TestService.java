package io.github.cmt.extension.consumer.service;

import io.github.cmt.extension.spi.IHelloService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@Service
public class TestService {

    @Resource(name = "IHelloService")
    IHelloService helloService;

    public String hello(){
        return helloService.hello();
    }
}
