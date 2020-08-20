package io.github.cmt.extension.core.bootstrap;

import io.github.cmt.extension.common.ConfigMode;
import io.github.cmt.extension.common.exception.SpiException;
import io.github.cmt.extension.core.configcenter.ConfigCenter;
import io.github.cmt.extension.common.model.SpiConfigDTO;
import io.github.cmt.extension.core.router.SpiRouter;

import io.github.cmt.extension.common.util.ApplicationContextHolder;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ClassUtils;

/**
 * @author shengchaojie
 * @date 2019-10-22
 **/
@Deprecated
public class SpiConsumerBootStrap implements ApplicationListener<ApplicationEvent>, BeanFactoryPostProcessor, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    @Setter
    private String appName;
    @Setter
    private ConfigMode configMode;
    private ConfigCenter configCenter = ConfigCenter.getInstance();

    public static SpiConsumerBootStrap create() {
        return new SpiConsumerBootStrap();
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
            SpiRouter.init();
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        configCenter.init(appName,configMode);
        //获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        defaultListableBeanFactory.registerBeanDefinition("SpringContextHolder", new RootBeanDefinition(ApplicationContextHolder.class));
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
                    defaultListableBeanFactory.registerBeanDefinition(clazz.getSimpleName(), beanDefinitionBuilder.getBeanDefinition());
                });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpiConsumerBootStrap.applicationContext = applicationContext;
    }

    public SpiConsumerBootStrap appName(String appName) {
        this.setAppName(appName);
        return this;
    }

    public SpiConsumerBootStrap configMode(ConfigMode configMode) {
        this.setConfigMode(configMode);
        return this;
    }
}
