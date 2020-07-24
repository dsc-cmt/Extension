package com.cmt.extension.core.configcenter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 配置变化事件
 *
 * @author tuzhenxian
 * @date 19-10-16
 */
@AllArgsConstructor
@Data
public class SpiConfigChangeEvent {
    private List<SpiConfigChangeDTO> configChanges;

    public static SpiConfigChangeEvent generateEvent(Application oldApp, Application newApp) {
        SpiConfigChangeEvent event = new SpiConfigChangeEvent(new ArrayList<>());

        if (newApp == null || newApp.getVersion() <= oldApp.getVersion()) return event;
        Map<String, SpiConfigDTO> oldConfigs = oldApp.buildConfigMap();
        Map<String, SpiConfigDTO> newConfigs = newApp.buildConfigMap();
        event.checkAddedOrModified(oldConfigs, newConfigs);
        event.checkDeleted(oldConfigs, newConfigs);
        return event;
    }

    private void checkAddedOrModified(Map<String, SpiConfigDTO> oldConfigs, Map<String, SpiConfigDTO> newConfigs) {
        for (Map.Entry<String, SpiConfigDTO> entry : newConfigs.entrySet()) {
            SpiConfigDTO oldConfig = oldConfigs.get(entry.getKey());
            if (oldConfig == null) {
                configChanges.add(new SpiConfigChangeDTO(entry.getValue(), SpiChangeType.ADDED));
            }
            if (oldConfig != null && entry.getValue().getVersion() > oldConfig.getVersion()) {
                configChanges.add(new SpiConfigChangeDTO(entry.getValue(), SpiChangeType.MODIFIED));
            }
        }
    }

    private void checkDeleted(Map<String, SpiConfigDTO> oldConfigs, Map<String, SpiConfigDTO> newConfigs) {
        for (Map.Entry<String, SpiConfigDTO> entry : oldConfigs.entrySet()) {
            SpiConfigDTO newConfig = newConfigs.get(entry.getKey());
            if (newConfig == null) {
                configChanges.add(new SpiConfigChangeDTO(entry.getValue(), SpiChangeType.DELETED));
            }
        }
    }

    @AllArgsConstructor
    @Data
    public static class SpiConfigChangeDTO {
        private SpiConfigDTO config;
        private SpiChangeType changeType;
    }

}
