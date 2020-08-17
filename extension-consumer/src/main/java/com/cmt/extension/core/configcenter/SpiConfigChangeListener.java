package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent;

/**
 * spi配置listener
 *
 * @author tuzhenxian
 * @date 19-10-16
 */
public interface SpiConfigChangeListener {
    /**
     * 配置变更触发动作
     *
     * @param event
     * @return
     */
    void onChange(SpiConfigChangeEvent event);
}
