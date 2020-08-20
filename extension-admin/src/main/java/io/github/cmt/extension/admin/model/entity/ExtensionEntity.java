package io.github.cmt.extension.admin.model.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

import io.github.cmt.extension.common.model.SpiConfigDTO;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * SPI扩展点实现
 *
 * @author yonghuang
 */
@Entity
@Table(name = "extension_extension")
@Data
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "spi")
public class ExtensionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务code
     **/
    private String bizCode;

    /**
     * 调用方式
     * 目前支持 本地、dubbo
     */
    private String invokeMethod;

    /**
     * 超时时间 (ms) 默认10s
     **/
    private Integer expireTime;

    /**
     * 是否为默认 1是 0 否
     **/
    private Integer isDefault;

    private String remark;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;

    @ManyToOne
    @JsonIgnore
    private SpiEntity spi;
    @Version
    private Integer version;

    public static ExtensionEntity create(SpiConfigDTO configDTO) {
        ExtensionEntity extension = new ExtensionEntity();
        extension.setBizCode(configDTO.getBizCode());
        extension.setInvokeMethod(configDTO.getInvokeMethod());
        extension.setExpireTime(configDTO.getExpireTime());
        extension.setIsDefault(configDTO.getIsDefault());
        extension.setRemark(configDTO.getRemark());
        return extension;
    }

    public void update(SpiConfigDTO configDTO) {
        this.setBizCode(configDTO.getBizCode());
        this.setInvokeMethod(configDTO.getInvokeMethod());
        this.setExpireTime(configDTO.getExpireTime());
        this.setIsDefault(configDTO.getIsDefault());
        this.setRemark(configDTO.getRemark());
        this.dateModified = new Date();
    }
}
