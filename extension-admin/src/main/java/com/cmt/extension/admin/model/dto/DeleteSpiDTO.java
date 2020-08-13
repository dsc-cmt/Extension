package com.cmt.extension.admin.model.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author tuzhenxian
 * @date 20-8-12
 */
@Data
public class DeleteSpiDTO {
    @NotNull(message = "应用名称不可为空")
    private String appName;
    @NotNull(message = "spi接口不可为空")
    private String spiInterface;
}
