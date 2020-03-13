package com.cmt.extension.provider.service;

import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.common.SpiTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@Extension(bizCode = "a", invokeMethod = SpiTypeEnum.DUBBO)
public class AHelloServiceImpl implements IHelloService {
    public String hello() {
        return "HelloA";
    }
}
