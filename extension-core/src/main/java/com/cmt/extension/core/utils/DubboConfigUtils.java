package com.cmt.extension.core.utils;

import com.alibaba.dubbo.config.*;
import com.cmt.extension.core.common.SpiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Dubbo配置获取工具类
 *
 * @author yonghuang
 */
@Slf4j
public class DubboConfigUtils {
    /**
     * 从spring容器获取注册中心
     * @return
     */
    public static List<? extends RegistryConfig> getRegestries() {
        ApplicationContext applicationContext = getApplicationContext();

        Map<String, RegistryConfig> registryConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, RegistryConfig.class, false, false);

        if (registryConfigMap == null || registryConfigMap.size() == 0) {
            log.error("没有获取到注册中心配置");
            throw new SpiException("没有获取到注册中心配置");
        }

        List<RegistryConfig> registryConfigs = new ArrayList<RegistryConfig>();

        for (RegistryConfig config : registryConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                registryConfigs.add(config);
            }
        }

        return registryConfigs;
    }

    /**
     * 从spring容器获取应用配置
     * @return
     */
    public static ApplicationConfig getApplicationConfig() {
        ApplicationContext applicationContext = getApplicationContext();

        Map<String, ApplicationConfig> applicationConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ApplicationConfig.class, false, false);

        if (applicationConfigMap == null || applicationConfigMap.size() == 0) {
            log.error("没有获取到应用配置");
            throw new SpiException("没有获取到应用配置");
        }

        ApplicationConfig applicationConfig = null;
        for (ApplicationConfig config : applicationConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                if (applicationConfig != null) {
                    throw new IllegalStateException("Duplicate application configs: " + applicationConfig + " and " + config);
                }
                applicationConfig = config;
            }
        }

        return applicationConfig;
    }

    /**
     * 从spring容器获取Consumer默认配置
     * @return
     */
    public static ConsumerConfig getConsumerConfig() {
        ApplicationContext applicationContext = getApplicationContext();
        Map<String, ConsumerConfig> consumerConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ConsumerConfig.class, false, false);

        if (consumerConfigMap == null || consumerConfigMap.size() == 0) {
            return null;
        }

        ConsumerConfig consumerConfig = null;
        for (ConsumerConfig config : consumerConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                if (consumerConfig != null) {
                    throw new IllegalStateException("Duplicate consumer configs: " + consumerConfig + " and " + config);
                }
                consumerConfig = config;
            }
        }

        return consumerConfig;
    }

    /**
     * 从spring容器获取ProviderConfig
     * @return
     */
    public static ProviderConfig getProviderConfig() {
        ApplicationContext applicationContext = getApplicationContext();
        Map<String, ProviderConfig> providerConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProviderConfig.class, false, false);

        if (providerConfigMap == null || providerConfigMap.size() == 0) {
            return null;
        }

        ProviderConfig providerConfig = null;
        for (ProviderConfig config : providerConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                if (providerConfig != null) {
                    throw new IllegalStateException("Duplicate provider configs: " + providerConfig + " and " + config);
                }
                providerConfig = config;
            }
        }

        return providerConfig;
    }

    /**
     * 从spring容器获取协议
     * @return
     */
    public static List<ProtocolConfig> getProtocolConfigs() {
        ApplicationContext applicationContext = getApplicationContext();
        Map<String, ProtocolConfig> protocolConfigMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext, ProtocolConfig.class, false, false);

        if (protocolConfigMap == null || protocolConfigMap.size() == 0) {
            return null;
        }

        List<ProtocolConfig> protocolConfigs = new ArrayList<ProtocolConfig>();
        for (ProtocolConfig config : protocolConfigMap.values()) {
            if (config.isDefault() == null || config.isDefault().booleanValue()) {
                protocolConfigs.add(config);
            }
        }

        return protocolConfigs;
    }

    private static ApplicationContext getApplicationContext() {
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();

        if (applicationContext == null) {
            log.error("ApplicationContextHolder.getApplicationContext() 为空, 代理dubbo服务失败");
            throw new SpiException("ApplicationContextHolder.getApplicationContext() 为空, 代理dubbo服务失败");
        }

        return applicationContext;
    }
}
