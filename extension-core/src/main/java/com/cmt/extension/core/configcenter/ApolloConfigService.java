package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.common.SpiException;
import com.cmt.extension.core.utils.EnvUtils;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.internals.DefaultConfig;
import com.ctrip.framework.apollo.internals.LocalFileConfigRepository;
import com.ctrip.framework.apollo.internals.RemoteConfigRepository;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * apollo配置工具类
 *
 * @author tuzhenxian
 * @date 19-10-8
 */

@Slf4j
public class ApolloConfigService {
    private static final ApolloConfigService INSTANCE = new ApolloConfigService();
    /**
     * apollo namespace
     */
    private String namespace;
    /**
     * apollo metaserver地址
     */
    private static String metaServer;
    /**
     * apollo config对象
     */
    private static Config config;

    private static String env;

    static {
        Properties props = new Properties();
        try {
            env = EnvUtils.getEnv().toUpperCase();
            props.load(ApolloConfigService.class.getResourceAsStream("/spi-env.properties"));
            switch (env) {
                case "DEV":
                    metaServer = props.getProperty("DEV");
                    break;
                case "PREPUB":
                    metaServer = props.getProperty("PREPUB");
                    break;
                case "PRO":
                    metaServer = props.getProperty("PRO");
                    break;
                case "STABLE":
                    metaServer = props.getProperty("STABLE");
                    break;
                default:
                    metaServer = props.getProperty("STABLE");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ApolloConfigService() {

    }

    public Map<String, SpiConfigDTO> syncConfig(String namespace) {
        if (StringUtils.isEmpty(namespace)) {
            throw SpiException.fail("未配置namespace!");
        }
        this.namespace = namespace;
        config=getConfig(namespace);
        if (config == null) {
            log.error("spi配置中心没有配置相应的namespace:{},请仔细核对或先创建相应的namespace", namespace);
            throw SpiException.fail("spi配置中心没有配置相应的namespace:" + namespace + ",请仔细核对或先创建相应的namespace");
        }
        Map<String, SpiConfigDTO> map = getAllConfigs()
                .entrySet()
                .stream()
                .map(entry -> SpiConfigDTO.buildConfigDTO(entry.getKey(), entry.getValue(), namespace))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SpiConfigDTO::buildKey, c -> c));
        log.info("[spi]缓存 {} spi扩展点信息共{}条", namespace, map.size());
        return map;
    }

    private Config getConfig(String namespace) {
        //暂存系统变量
        String oldApolloMeta= System.getProperty("apollo.meta");
        String oldAppId=System.getProperty("app.id");
        System.setProperty("apollo.meta", metaServer);
        System.setProperty("app.id", "spi-admin");
        log.info("[spi]:获取SPI扩展点配置,namespace:{} , env:{}", namespace, env);
        //避免覆盖项目原生 apollo中ConfigManager的配置
        config =new DefaultConfig(namespace, new LocalFileConfigRepository(namespace, new RemoteConfigRepository(namespace)));
        clearIfAbsent("apollo.meta",oldApolloMeta);
        clearIfAbsent("app.id",oldAppId);
        return config;
    }

    private void clearIfAbsent(String key, String value) {
        if(value==null){
            System.clearProperty(key);
            return;
        }
        System.setProperty(key,value);
    }

    /**
     * 获取所有配置信息
     *
     * @param
     * @return
     */
    private static Map<String, String> getAllConfigs() {
        Map<String, String> map = new HashMap<>();
        Set<String> keys = config.getPropertyNames();
        if (CollectionUtils.isEmpty(keys)) {
            return map;
        }
        for (String key : keys) {
            String value = config.getProperty(key, "");
            map.put(key, value);
        }
        return map;
    }

    public static ApolloConfigService getInstance() {
        return INSTANCE;
    }

    public static Config getApolloConfig() {
        return config;
    }
}
