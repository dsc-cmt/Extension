package io.github.cmt.extension.core.config;

import io.github.cmt.extension.core.annotation.EnableExtensionConsumer;
import io.github.cmt.extension.core.bootstrap.ExtensionInitializer;
import io.github.cmt.extension.common.ConfigMode;
import io.github.cmt.extension.common.util.ApplicationContextHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author shengchaojie
 * @date 2020/8/16
 **/
@Configuration
public class ExtensionConfiguration implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableExtensionConsumer.class.getName()));
        String appName = annoAttrs.getString("appName");
        ConfigMode configMode = annoAttrs.getEnum("configMode");

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ExtensionInitializer.class);
        beanDefinitionBuilder.addConstructorArgValue(appName).addConstructorArgValue(configMode);
        registry.registerBeanDefinition(ExtensionInitializer.class.getSimpleName(),beanDefinitionBuilder.getBeanDefinition());

        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ApplicationContextHolder.class);
        registry.registerBeanDefinition(ApplicationContextHolder.class.getSimpleName(),beanDefinitionBuilder.getBeanDefinition());
    }
}
