package com.cmt.extension.core.provider;

import com.cmt.extension.core.call.WrapperGeneratorComposite;
import com.cmt.extension.core.common.ExtesionTypeEnum;
import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.provider.dubbo.DubboServiceExporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;

/**
 * 处理Extension注解，暴露服务
 *
 * @author yonghuang
 */

@Slf4j
public class ExtensionAnnotationPostProcessor implements BeanPostProcessor, Ordered {

    private static final boolean DUBBO_PRESENT = ClassUtils.isPresent("com.alibaba.dubbo.common.URL", WrapperGeneratorComposite.class.getClassLoader());

    private DubboServiceExporter dubboServiceExporter;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        if (AopUtils.getTargetClass(bean).isAnnotationPresent(Extension.class)) {
            Extension extension = AopUtils.getTargetClass(bean).getAnnotation(Extension.class);
            if (ExtesionTypeEnum.DUBBO.equals(extension.invokeMethod())) {
                dubboServiceExporter.exportService(bean);
            }
        }

        return bean;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @PostConstruct
    public void init() {
        if (DUBBO_PRESENT) {
            dubboServiceExporter = new DubboServiceExporter();
        } else {
            log.info("spi框架支持invokeMethod为dubbo的方式, 但是未加入dubbo依赖包");
        }
    }
}
