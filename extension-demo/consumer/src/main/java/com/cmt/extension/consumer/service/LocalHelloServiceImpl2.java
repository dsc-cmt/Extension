package com.cmt.extension.consumer.service;

import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.common.SpiTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-25
 **/
@Extension(bizCode = "f",invokeMethod = SpiTypeEnum.LOCAL)
public class LocalHelloServiceImpl2 implements IHelloService {
    public String hello() {
        return "local2";
    }
}
