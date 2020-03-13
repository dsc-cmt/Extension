package com.cmt.extension.core.call;

import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

/**
 *
 */
public interface WrapperGenerator {

    void preCheck();

    boolean support(SpiConfigDTO configDTO);
    /**
     * 根据协议生成包装类
     * @param configDTO
     * @return
     */
    Object genericWrapper(SpiConfigDTO configDTO);

    void destroyWrapper(SpiConfigDTO spiConfigDTO);
}
