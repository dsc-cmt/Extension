package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.SpiChangeType;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeDTO;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * @author tuzhenxian
 * @date 19-10-16
 */
@Slf4j
public class ConfigCenter {
    private static final ConfigCenter INSTANCE = new ConfigCenter();
    /**
     * SPI 配置缓存
     */
    private final Map<String, SpiConfigDTO> configMap = new ConcurrentHashMap<>();
    private final List<SpiConfigChangeListener> listeners = new ArrayList<>();
    private ConfigService configService = new ZooKeeperConfigServiceImpl();
    private Boolean inited = false;

    private ConfigCenter() {
    }

    public static ConfigCenter getInstance() {
        return INSTANCE;
    }

    public void addListener(SpiConfigChangeListener listener) {
        listeners.add(listener);
        fireListenerInitEvent(listener);
    }

    private void fireConfigChangeEvent(SpiConfigChangeEvent event) {
        for (SpiConfigChangeListener listener : listeners) {
            listener.onChange(event);
        }
    }

    /**
     * 获取所有spi配置
     *
     * @return
     */
    public List<SpiConfigDTO> getAllSpiConfigDTO() {
        return Lists.newArrayList(configMap.values());
    }

    /**
     * 添加listener先同步所有配置
     *
     * @param listener
     * @return
     */
    private void fireListenerInitEvent(SpiConfigChangeListener listener) {
        if (CollectionUtils.isEmpty(configMap)) {
            return;
        }
        List<SpiConfigChangeDTO> configChangeList = this.configMap.values().stream()
                .map(config -> new SpiConfigChangeDTO(config, SpiChangeType.INIT))
                .collect(Collectors.toList());
        listener.onChange(new SpiConfigChangeEvent(configChangeList));
    }

    private void initConfig(Map<String, SpiConfigDTO> configMap) {
        this.configMap.putAll(configMap);
    }

    public void changeConfig(SpiConfigChangeDTO configChange) {
        if (configChange==null) {
            return;
        }
        //更新缓存
        SpiConfigDTO config = configChange.getConfig();
        if (configChange.getChangeType() == SpiChangeType.DELETED) {
            configMap.remove(config.buildKey());
        } else {
            configMap.put(config.buildKey(), config);
        }
        //触发事件
        fireConfigChangeEvent(new SpiConfigChangeEvent(Arrays.asList(configChange)));
    }

    public void init(String namespace) {
        if (inited) {
            return;
        }
        //缓存到configCenter
        initConfig(configService.syncConfig(namespace));
        configService.onChange(this::changeConfig);

        inited = true;
    }
}
