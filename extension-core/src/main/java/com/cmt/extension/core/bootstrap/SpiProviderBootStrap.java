package com.cmt.extension.core.bootstrap;

import com.cmt.extension.core.provider.ExtensionAnnotationPostProcessor;
import com.cmt.extension.core.utils.ApplicationContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author shengchaojie
 * @date 2019-10-22
 **/
public class SpiProviderBootStrap implements BeanDefinitionRegistryPostProcessor {

    public static SpiProviderBootStrap create(){
        return new SpiProviderBootStrap();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registry.registerBeanDefinition("SpringContextHolder",new RootBeanDefinition(ApplicationContextHolder.class));
        RootBeanDefinition bd = new RootBeanDefinition(ExtensionAnnotationPostProcessor.class);
        registry.registerBeanDefinition("ExtensionAnnotationPostProcessor",bd);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
