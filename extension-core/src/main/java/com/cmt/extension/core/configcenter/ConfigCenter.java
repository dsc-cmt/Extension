package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.Application;
import com.cmt.extension.core.configcenter.model.SpiChangeType;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent;
import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent.SpiConfigChangeDTO;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.ArrayList;
import java.util.List;
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

    private final ConfigService configService = new LocalConfigServiceImpl();
    private final List<SpiConfigChangeListener> listeners = new ArrayList<>();
    /**
     * 应用配置缓存
     */
    private Application application;

    private ConfigCenter() {
    }

    public Application getCachedApplication(){
        return application;
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
        return application.buildConfigs();
    }

    /**
     * 添加listener先同步所有配置
     *
     * @param listener
     * @return
     */
    private void fireListenerInitEvent(SpiConfigChangeListener listener) {
        if (CollectionUtils.isEmpty(application.buildConfigs())) {
            return;
        }
        List<SpiConfigChangeDTO> configChangeList = application.buildConfigs().stream()
                .map(config -> new SpiConfigChangeDTO(config, SpiChangeType.INIT))
                .collect(Collectors.toList());
        listener.onChange(new SpiConfigChangeEvent(configChangeList));
    }

    public void changeConfig(SpiConfigChangeEvent event) {
        List<SpiConfigChangeDTO> configChanges=event.getConfigChanges();
        if (CollectionUtils.isEmpty(configChanges)) {
            return;
        }
        //触发事件
        fireConfigChangeEvent(new SpiConfigChangeEvent(configChanges));
    }

    public void init(String appName) {
        this.application = configService.init(appName);
        configService.periodicRefresh();
        application.addListener(this::changeConfig);
    }
}
