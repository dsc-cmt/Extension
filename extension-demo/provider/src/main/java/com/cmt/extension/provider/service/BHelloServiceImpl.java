package com.cmt.extension.provider.service;

import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@Extension(bizCode = "b", invokeMethod = ExtensionTypeEnum.DUBBO)
public class BHelloServiceImpl implements IHelloService {
    public String hello() {
        return "helloB";
    }
}
