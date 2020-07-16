package com.cmt.extension.admin.model.entity;

import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * SPI扩展点实现
 *
 * @author yonghuang
 */
@Entity
@Table(name = "extension_extension")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ExtensionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 业务code **/
    private String bizCode;

    /**
     * 调用方式
     * 目前支持 本地、dubbo
     */
    private String invokeMethod;

    /** 超时时间 (ms) 默认10s **/
    private Integer expireTime;

    /** 是否为默认 1是 0 否 **/
    private Integer isDefault;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;

    @ManyToOne
    private SpiEntity spi;
    @Version
    private Integer version;

    public static ExtensionEntity create(SpiConfigDTO configDTO) {
        ExtensionEntity extension = new ExtensionEntity();
        extension.setBizCode(configDTO.getBizCode());
        extension.setInvokeMethod(configDTO.getInvokeMethod());
        extension.setExpireTime(configDTO.getExpireTime());
        extension.setIsDefault(configDTO.getIsDefault());

        return extension;
    }

    public void update(SpiConfigDTO configDTO) {
        this.setBizCode(configDTO.getBizCode());
        this.setInvokeMethod(configDTO.getInvokeMethod());
        this.setExpireTime(configDTO.getExpireTime());
        this.setIsDefault(configDTO.getIsDefault());
    }
}
