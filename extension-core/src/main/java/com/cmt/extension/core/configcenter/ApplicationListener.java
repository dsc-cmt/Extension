package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.SpiConfigChangeEvent;

/**
 * @author tuzhenxian
 * @date 20-7-24
 */
public interface ApplicationListener {
    /**
     * 监听 application变化
     * @param application
     * @return
     */
    void onChange(SpiConfigChangeEvent application);
}
