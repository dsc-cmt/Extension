package com.cmt.extension.core.router;

import io.github.cmt.extension.common.BusinessContext;
import io.github.cmt.extension.common.annotation.Extension;
import com.cmt.extension.core.configcenter.ConfigCenter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class SpiRouter {

    private static ConfigCenter configCenter = ConfigCenter.getInstance();

    private static SpiRegistry spiRegistry = SpiRegistry.getRegistry();

    private static Boolean inited = false;

    public static Object route(String spi, Method method, Object[] args) throws Throwable {
        Object impl = Optional.ofNullable(SpiContainer.get(spi, BusinessContext.getBizCode())).orElse(SpiContainer.get(spi, Extension.DEFAULT_BIZ_CODE));
        if (impl == null) {
            throw new RuntimeException("找不到spi实现类");
        }
        try {
            return method.invoke(impl, args);
        } catch (Exception ex){
            throw ex.getCause();
        }
    }

    public static void init(){
        if(!inited){
            configCenter.addListener(spiRegistry);
            inited = true;
        }
    }
}
