package io.github.cmt.extension.core.bootstrap;

import io.github.cmt.extension.core.router.SpiRouter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * SPI代理生成类
 *
 * @author yonghuang
 */
public class SpiFactoryBean<T> implements FactoryBean<T> {
    private Class<T> spiInterface;

    public SpiFactoryBean(Class<T> spiInterface) {
        this.spiInterface = spiInterface;
    }

    @Override
    public T getObject() {
        // jdk动态代理类生成
        InvocationHandler invocationHandler = (proxy, method, args) -> SpiRouter.route(spiInterface.getName(), method, args);
        return spiInterface.cast(Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{spiInterface},
                invocationHandler));
    }

    @Override
    public Class<?> getObjectType() {
        return spiInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
