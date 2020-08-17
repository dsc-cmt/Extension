package io.github.cmt.extension.provider;

import io.github.cmt.extension.common.ExtensionTypeEnum;
import io.github.cmt.extension.common.annotation.Extension;
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

    private static final boolean DUBBO_PRESENT = ClassUtils.isPresent("com.alibaba.dubbo.common.URL", ExtensionAnnotationPostProcessor.class.getClassLoader());

    private DubboServiceExporter dubboServiceExporter;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (AopUtils.getTargetClass(bean).isAnnotationPresent(Extension.class)) {
            Extension extension = AopUtils.getTargetClass(bean).getAnnotation(Extension.class);
            if (ExtensionTypeEnum.DUBBO.equals(extension.invokeMethod())) {
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
