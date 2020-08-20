package io.github.cmt.extension.core.call;

import io.github.cmt.extension.common.model.SpiConfigDTO;

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
    Object generateWrapper(SpiConfigDTO configDTO);

    void destroyWrapper(SpiConfigDTO spiConfigDTO);
}
