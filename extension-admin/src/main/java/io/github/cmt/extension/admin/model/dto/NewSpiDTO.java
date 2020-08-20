package io.github.cmt.extension.admin.model.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author tuzhenxian
 * @date 20-7-23
 */
@Data
public class NewSpiDTO {
    /**
     * 接口全限定名
     */
    @NotNull(message = "接口名不可为空")
    private String spiInterface;
    /**
     * 描述
     */
    private String desc;
    /**
     * 应用名
     */
    @NotNull(message = "应用名不可为空")
    private String appName;
}
