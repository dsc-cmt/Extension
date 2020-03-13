package com.cmt.extension.core.provider.dubbo;

import com.alibaba.dubbo.config.*;
import com.cmt.extension.core.annotation.Extension;
import com.cmt.extension.core.annotation.SPI;
import com.cmt.extension.core.common.SpiException;
import com.cmt.extension.core.utils.ApplicationContextHolder;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Class<?>> spiInterfaces=ClassUtils.getAllInterfaces(clazz).stream().filter(c->c.isAnnotationPresent(SPI.class)).collect(Collectors.toList());
        if(spiInterfaces.size()==0){
            throw SpiException.fail(AopUtils.getTargetClass(bean).getName()+"使用了@Extension标注,但未继承SPI接口!");
        }
        if(spiInterfaces.size()>1){
            throw SpiException.fail(AopUtils.getTargetClass(bean).getName()+"继承了多个spi接口请检查!");
        }
        ServiceConfig<Object> serviceConfig = new ServiceConfig<>();
        serviceConfig.setGroup(extension.bizCode());
        serviceConfig.setInterface(spiInterfaces.get(0));

        //获取spring容器 目前下面的配置是让使用方使用dubbo.xml配置一份 后期可以优化 不依赖spring 使用自己的配置文件构造后传入
            ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
            if (applicationContext == null) {
                log.error("ApplicationContextHolder.getApplicationContext() 为空, 暴露dubbo服务失败");
                throw new SpiException("ApplicationContextHolder.getApplicationContext() 为空, 暴露dubbo服务失败");
            }

            //获取ApplicationConfig
            if (serviceConfig.getApplication() == null
                    && (serviceConfig.getProvider() == null || serviceConfig.getProvider().getApplication() == null)) {
                Map<String, ApplicationConfig> applicationConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);
                if (applicationConfigMap != null && applicationConfigMap.size() > 0) {
                    ApplicationConfig applicationConfig = null;
                    for (ApplicationConfig config : applicationConfigMap.values()) {
                        if (config.isDefault() == null || config.isDefault().booleanValue()) {
                            if (applicationConfig != null) {
                                throw new IllegalStateException("Duplicate application configs: " + applicationConfig + " and " + config);
                            }
                            applicationConfig = config;
                        }
                    }
                    if (applicationConfig != null) {
                        serviceConfig.setApplication(applicationConfig);
                    }
                }
            }
            //从spring容器获取ProviderConfig
            if (serviceConfig.getProvider() == null) {
                Map<String, ProviderConfig> providerConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProviderConfig.class, false, false);
                if (providerConfigMap != null && providerConfigMap.size() > 0) {
                    Map<String, ProtocolConfig> protocolConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
                    if ((protocolConfigMap == null || protocolConfigMap.size() == 0)
                            && providerConfigMap.size() > 1) { // 兼容旧版本
                        List<ProviderConfig> providerConfigs = new ArrayList<ProviderConfig>();
                        for (ProviderConfig config : providerConfigMap.values()) {
                            if (config.isDefault() != null && config.isDefault().booleanValue()) {
                                providerConfigs.add(config);
                            }
                        }
                        if (providerConfigs.size() > 0) {
                            serviceConfig.setProviders(providerConfigs);
                        }
                    } else {
                        ProviderConfig providerConfig = null;
                        for (ProviderConfig config : providerConfigMap.values()) {
                            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                                if (providerConfig != null) {
                                    throw new IllegalStateException("Duplicate provider configs: " + providerConfig + " and " + config);
                                }
                                providerConfig = config;
                            }
                        }
                        if (providerConfig != null) {
                            serviceConfig.setProvider(providerConfig);
                        }
                    }
                }
            }
            //从spring容器获取注册中心
            if ((serviceConfig.getRegistries() == null || serviceConfig.getRegistries().size() == 0)
                    && (serviceConfig.getProvider() == null || serviceConfig.getProvider().getRegistries() == null || serviceConfig.getProvider().getRegistries().size() == 0)
                    && (serviceConfig.getApplication() == null || serviceConfig.getApplication().getRegistries() == null || serviceConfig.getApplication().getRegistries().size() == 0)) {
                Map<String, RegistryConfig> registryConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);
                if (registryConfigMap != null && registryConfigMap.size() > 0) {
                    List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();
                    for (RegistryConfig config : registryConfigMap.values()) {
                        if (config.isDefault() == null || config.isDefault().booleanValue()) {
                            registryConfigs.add(config);
                        }
                    }
                    if (registryConfigs != null && registryConfigs.size() > 0) {
                        serviceConfig.setRegistries(registryConfigs);
                    }
                }
            }
            //从spring容器获取协议
            if ((serviceConfig.getProtocols() == null || serviceConfig.getProtocols().size() == 0)
                    && (serviceConfig.getProvider() == null || serviceConfig.getProvider().getProtocols() == null || serviceConfig.getProvider().getProtocols().size() == 0)) {
                Map<String, ProtocolConfig> protocolConfigMap = applicationContext == null ? null : BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);
                if (protocolConfigMap != null && protocolConfigMap.size() > 0) {
                    List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
                    for (ProtocolConfig config : protocolConfigMap.values()) {
                        if (config.isDefault() == null || config.isDefault().booleanValue()) {
                            protocolConfigs.add(config);
                        }
                    }
                    if (protocolConfigs != null && protocolConfigs.size() > 0) {
                        serviceConfig.setProtocols(protocolConfigs);
                    }
                }
            }
            serviceConfig.setRef(bean);
            serviceConfig.export();
        return bean;
    }
}
