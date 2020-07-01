package com.cmt.extension.core.configcenter.model;

import java.util.List;

/**
 * 配置变化事件
 *
 * @author tuzhenxian
 * @date 19-10-16
 */
public class SpiConfigChangeEvent {
    private List<SpiConfigChangeDTO> configChanges;

    public SpiConfigChangeEvent(List<SpiConfigChangeDTO> configChanges) {
        this.configChanges = configChanges;
    }

    public List<SpiConfigChangeDTO> getConfigChanges() {
        return configChanges;
    }

    public void setConfigChanges(List<SpiConfigChangeDTO> configChanges) {
        this.configChanges = configChanges;
    }
}
