package com.cmt.extension.admin.model.vo;

import com.cmt.extension.admin.model.type.YesOrNoEnum;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author tuzhenxian
 * @date 19-10-9
 */
@Data
public class SpiConfigVO {
    private static final int DEFAULT_EXPIRE_TIME = 10000;
    /**
     * spi接口
     */
    @NotNull(message = "spi接口不可为空",groups = {AddOrUpdateValidate.class,DeleteValidate.class})
    private String spiInterface;
    /**
     * 业务code
     */
    @NotNull(message = "bizCode不可为空",groups = {AddOrUpdateValidate.class,DeleteValidate.class})
    private String bizCode;
    /**
     * 调用方式
     * 目前支持 本地、dubbo
     */
    @NotNull(message = "调用方式不可为空",groups = {AddOrUpdateValidate.class})
    private String invokeMethod;
    /**
     * 应用名
     */
    @NotNull(message = "appName不可为空",groups = {AddOrUpdateValidate.class,DeleteValidate.class})
    private String appName;

    /**
     * 超时时间 (ms) 默认10s
     */
    private Integer expireTime;
    /**
     *是否为默认 1是 0 否
     */
    private Integer isDefault;

    private String isDefaultDesc;

    /**
     * 描述
     */
    private String comment;

    private String mobile;

    public SpiConfigDTO buildConfigDTO() {
        SpiConfigDTO dto = new SpiConfigDTO();
        dto.setExpireTime(this.expireTime==null?DEFAULT_EXPIRE_TIME:this.expireTime);
        dto.setInvokeMethod(this.invokeMethod);
        dto.setBizCode(this.bizCode);
        dto.setSpiInterface(this.spiInterface);
        dto.setAppName(this.appName);
        dto.setIsDefault(this.isDefault);
        dto.setComment(this.comment);
        return dto;
    }

    public static SpiConfigVO buildByConfigDTO(SpiConfigDTO dto){
        SpiConfigVO vo=new SpiConfigVO();
        vo.setSpiInterface(dto.getSpiInterface());
        vo.setInvokeMethod(dto.getInvokeMethod());
        vo.setBizCode(dto.getBizCode());
        vo.setExpireTime(dto.getExpireTime());
        vo.setIsDefault(dto.getIsDefault());
        vo.setIsDefaultDesc(YesOrNoEnum.getDescByCode(dto.getIsDefault()));
        vo.setAppName(dto.getAppName());

        return vo;
    }

    public interface AddOrUpdateValidate{}

    public interface DeleteValidate{}

}
