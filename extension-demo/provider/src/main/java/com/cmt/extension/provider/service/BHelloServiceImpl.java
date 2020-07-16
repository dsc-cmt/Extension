package com.cmt.extension.provider.service;

import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.common.ExtesionTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author shengchaojie
 * @date 2019-10-18
 **/
@Extension(bizCode = "b", invokeMethod = ExtesionTypeEnum.DUBBO)
public class BHelloServiceImpl implements IHelloService {
    public String hello() {
        return "helloB";
    }
}
