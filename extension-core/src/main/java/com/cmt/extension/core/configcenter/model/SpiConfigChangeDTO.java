package com.cmt.extension.core.configcenter.model;

/**
 * @author tuzhenxian
 * @date 20-6-30
 */
public class SpiConfigChangeDTO {
    private SpiConfigDTO config;
    private SpiChangeType changeType;

    public SpiConfigChangeDTO(SpiConfigDTO config, SpiChangeType changeType) {
        this.config = config;
        this.changeType = changeType;
    }

    public SpiConfigDTO getConfig() {
        return config;
    }

    public void setConfig(SpiConfigDTO config) {
        this.config = config;
    }

    public SpiChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(SpiChangeType changeType) {
        this.changeType = changeType;
    }
}