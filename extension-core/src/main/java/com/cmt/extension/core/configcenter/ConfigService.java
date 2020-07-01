package com.cmt.extension.core.configcenter;

import com.cmt.extension.core.configcenter.model.SpiConfigChangeDTO;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author tuzhenxian
 * @date 20-6-30
 */
public interface ConfigService {
    /**
     * 同步命名空间下的所有配置
     *
     * @param namespace
     * @return
     */
    Map<String, SpiConfigDTO> syncConfig(String namespace);

    /**
     * 配置变更
     *
     * @param consumer
     * @return
     */
    void onChange(Consumer<SpiConfigChangeDTO> consumer);
}
