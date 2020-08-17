package com.cmt.extension.consumer.service;

import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-25
 **/
@Extension(bizCode = "d",invokeMethod = ExtensionTypeEnum.LOCAL)
public class LocalHelloServiceImpl implements IHelloService {
    public String hello() {
        return "helloD";
    }
}
