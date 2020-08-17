package com.cmt.extension.core.configcenter;

import io.github.cmt.extension.common.model.Application;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public interface ConfigService {
    /**
     * 初始化配置
     *
     * @param appName
     * @return
     */
    Application init(String appName);

    /**
     * 定时刷新配置
     *
     * @param
     * @return
     */
    default void periodicRefresh() {

    }
}
