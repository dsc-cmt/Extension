package com.cmt.extension.core.configcenter.model;

/**
 * @author tuzhenxian
 * @date 20-7-20
 */
public class Extension {
    private String bizCode;
    private String invokeMethod;
    private Integer expireTime;
    private boolean isDefault;
    private Integer version;

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public SpiConfigDTO build(String appName, String spiInterface) {
        SpiConfigDTO dto = new SpiConfigDTO();
        dto.setBizCode(bizCode);
        dto.setInvokeMethod(invokeMethod);
        dto.setIsDefault(isDefault ? 1 : 0);
        dto.setExpireTime(expireTime);
        dto.setSpiInterface(spiInterface);
        dto.setAppName(appName);
        dto.setVersion(version);
        return dto;
    }
}
