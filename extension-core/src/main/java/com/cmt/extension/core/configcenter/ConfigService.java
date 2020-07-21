package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.Map;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public interface ConfigService {
    /**
     * 载入配置
     *
     * @param appName
     * @return
     */
    Map<String, SpiConfigDTO> loadConfig(String appName);
}
