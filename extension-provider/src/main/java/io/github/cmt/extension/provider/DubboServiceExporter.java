package io.github.cmt.extension.provider;

import com.alibaba.dubbo.config.ServiceConfig;
import io.github.cmt.extension.common.annotation.Extension;
import io.github.cmt.extension.common.annotation.SPI;
import io.github.cmt.extension.common.exception.SpiException;
import io.github.cmt.extension.common.util.DubboConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * dubbo服务暴露
 *
 * @author yonghuang
 */
@Slf4j
public class DubboServiceExporter {

    public Object exportService(Object bean) {
        Class<?> clazz=AopUtils.getTargetClass(bean);
        Extension extension = clazz.getAnnotation(Extension.class);
        if (extension == null) {
            return bean;
        }
        //获取Extension标注的实现类继承的spi接口
        List<Class<?>> spiInterfaces= Arrays.stream(ClassUtils.getAllInterfacesForClass(clazz)).filter(c->c.isAnnotationPresent(SPI.class)).collect(Collectors.toList());
        if(spiInterfaces.size()==0){
            throw SpiException.fail(AopUtils.getTargetClass(bean).getName()+"使用了@Extension标注,但未继承SPI接口!");
        }

        // TODO: 2020/8/17 可以实现多个spi接口
        if(spiInterfaces.size()>1){
            throw SpiException.fail(AopUtils.getTargetClass(bean).getName()+"继承了多个spi接口请检查!");
        }
        ServiceConfig<Object> serviceConfig = new ServiceConfig<>();
        serviceConfig.setGroup(extension.bizCode());
        serviceConfig.setInterface(spiInterfaces.get(0));
        //获取ApplicationConfig
        serviceConfig.setApplication(DubboConfigUtils.getApplicationConfig());
        //从spring容器获取ProviderConfig
        serviceConfig.setProvider(DubboConfigUtils.getProviderConfig());
        //从spring容器获取注册中心
        serviceConfig.setRegistries(DubboConfigUtils.getRegestries());
        //从spring容器获取协议
        serviceConfig.setProtocols(DubboConfigUtils.getProtocolConfigs());

        serviceConfig.setRef(bean);
        serviceConfig.export();
        return bean;
    }
}
