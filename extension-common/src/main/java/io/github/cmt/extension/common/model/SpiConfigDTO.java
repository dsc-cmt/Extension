package io.github.cmt.extension.common.model;

import lombok.Data;

/**
 * @author tuzhenxian
 * @date 19-10-11
 */
@Data
public class SpiConfigDTO {
    private static final String DEFAULT_SPILITOR = "_";
    /**
     * spi接口
     */
    private String spiInterface;
    /**
     * 业务code
     */
    private String bizCode;
    /**
     * 调用方式
     * 目前支持 本地、dubbo
     */
    private String invokeMethod;
    /**
     * 应用名
     */
    private String appName;

    /**
     * 扩展点id
     */
    private Long extensionId;

    /**
     * 超时事件 (ms)
     */
    private Integer expireTime;
    /**
     * 是否为默认实现
     */
    private Integer isDefault;

    private String remark;

    private Integer version;

    /**
     * 根据参数构建key
     *
     * @param
     * @return key格式： bizcode_spiInterface
     */
    public String buildKey() {
        return this.bizCode + DEFAULT_SPILITOR + this.spiInterface;
    }
}
