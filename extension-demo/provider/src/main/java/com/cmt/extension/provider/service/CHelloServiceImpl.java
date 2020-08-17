package com.cmt.extension.provider.service;

import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.ExtensionTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author FD
 * @date 2019-10-22
 **/
@Extension(bizCode = "c", invokeMethod = ExtensionTypeEnum.DUBBO)
public class CHelloServiceImpl implements IHelloService {
    public String hello() {
        return "helloC";
    }
}
