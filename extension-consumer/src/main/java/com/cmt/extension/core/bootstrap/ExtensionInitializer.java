package com.cmt.extension.core.bootstrap;

import io.github.cmt.extension.common.ConfigMode;
import com.cmt.extension.core.configcenter.ConfigCenter;
import io.github.cmt.extension.common.model.SpiConfigDTO;
import com.cmt.extension.core.router.SpiRouter;
import io.github.cmt.extension.common.exception.SpiException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ClassUtils;

/**
 * @author shengchaojie
 * @date 2020/8/17
 **/
public class ExtensionInitializer implements BeanDefinitionRegistryPostProcessor, ApplicationListener<ContextRefreshedEvent> {

    private String appName;
    private ConfigMode configMode;
    private ConfigCenter configCenter = ConfigCenter.getInstance();

    public ExtensionInitializer(String appName, ConfigMode configMode) {
        this.appName = appName;
        this.configMode = configMode;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        configCenter.init(appName,configMode);
        configCenter.getAllSpiConfigDTO().stream().map(SpiConfigDTO::getSpiInterface)
                .distinct()
                .forEach(i -> {
                    Class clazz = null;
                    try {
                        clazz = ClassUtils.forName(i, Thread.currentThread().getContextClassLoader());
                    } catch (ClassNotFoundException e) {
                        throw new SpiException("获取spi接口失败");
                    }
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpiFactoryBean.class);
                    beanDefinitionBuilder.addConstructorArgValue(clazz);
                    registry.registerBeanDefinition(clazz.getSimpleName(), beanDefinitionBuilder.getBeanDefinition());
                });
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        SpiRouter.init();
    }
}
