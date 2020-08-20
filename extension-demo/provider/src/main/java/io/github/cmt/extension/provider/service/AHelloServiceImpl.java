package io.github.cmt.extension.provider.service;

import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import io.github.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@Extension(bizCode = "a", invokeMethod = ExtensionTypeEnum.DUBBO)
public class AHelloServiceImpl implements IHelloService {
    public String hello() {
        return "HelloA";
    }
}
