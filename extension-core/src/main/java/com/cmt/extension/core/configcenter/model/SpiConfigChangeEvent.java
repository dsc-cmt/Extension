package com.cmt.extension.core.configcenter.model;

import java.util.List;
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

    @AllArgsConstructor
    @Data
    public static class SpiConfigChangeDTO{
        private SpiConfigDTO config;
        private SpiChangeType changeType;
    }
}
