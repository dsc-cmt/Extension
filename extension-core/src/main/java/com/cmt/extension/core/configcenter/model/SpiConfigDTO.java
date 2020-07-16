package com.cmt.extension.core.configcenter.model;

import lombok.Data;
import org.springframework.util.StringUtils;

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
     * 应用ID
     */
    private Long appId;

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

    private String comment;

    public static SpiConfigDTO buildConfigDTO(String key, String value, String appName) {
        SpiConfigDTO dto = new SpiConfigDTO();
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return null;
        }
        String[] keySplits = key.split(DEFAULT_SPILITOR);
        String[] valueSplits = value.split(DEFAULT_SPILITOR);
        if (keySplits.length != 2 || valueSplits.length <2) {
            return null;
        }
        dto.setAppName(appName);
        dto.setBizCode(keySplits[0]);
        dto.setSpiInterface(keySplits[1]);
        dto.setInvokeMethod(valueSplits[0]);
        dto.setExpireTime(Integer.valueOf(valueSplits[1]));
        dto.setIsDefault(valueSplits.length<3?0:Integer.valueOf(valueSplits[2]));

        return dto;
    }

    /**
     * 根据参数构建key
     *
     * @param
     * @return key格式： bizcode_spiInterface
     */
    public String buildKey() {
        return this.bizCode + DEFAULT_SPILITOR + this.spiInterface;
    }

    /**
     * 根据参数构建value
     *
     * @param
     * @return value 格式：invokeMethod_expireTime
     */
    public String buildValue() {
        return this.invokeMethod + DEFAULT_SPILITOR + this.expireTime +DEFAULT_SPILITOR+ this.isDefault;
    }
}
