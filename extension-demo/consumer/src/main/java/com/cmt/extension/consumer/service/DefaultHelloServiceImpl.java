package com.cmt.extension.consumer.service;

import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.common.ExtesionTypeEnum;
import com.cmt.extension.spi.IHelloService;
import static com.cmt.extension.core.annotation.Extension.DEFAULT_BIZ_CODE;

/**
 * @author shengchaojie
 * @date 2019-11-04
 **/
@Extension(bizCode = DEFAULT_BIZ_CODE,invokeMethod = ExtesionTypeEnum.LOCAL)
public class DefaultHelloServiceImpl implements IHelloService {
    public String hello() {
        return "default hello";
    }
}
