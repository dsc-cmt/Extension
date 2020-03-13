package com.cmt.extension.provider.service;

import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.common.SpiTypeEnum;
import com.cmt.extension.spi.IHelloService;

/**
 * @author FD
 * @date 2019-10-22
 **/
@Extension(bizCode = "c", invokeMethod = SpiTypeEnum.DUBBO)
public class CHelloServiceImpl implements IHelloService {
    public String hello() {
        return "helloC";
    }
}
