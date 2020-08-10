package com.cmt.extension.admin.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

/**
 * SPI
 *
 * @author yonghuang
 */
@Entity
@Table(name = "extension_spi")
@Data
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = {"extensions","app"})
public class SpiEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * spi接口
     **/
    private String spiInterface;

    /**
     * 描述
     **/
    private String description;

    /**
     * SPI扩展点实现
     **/
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "spi", orphanRemoval = true)
    private List<ExtensionEntity> extensions = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    private AppEntity app;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;
    @Version
    private Integer version;

    public static SpiEntity create(String spiInterface, String desc) {
        SpiEntity spi = new SpiEntity();
        spi.setSpiInterface(spiInterface);
        spi.setDescription(desc);
        return spi;
    }

    public void updateExtension(SpiConfigDTO configDTO) {
        for (ExtensionEntity e : extensions) {
            if (e.getId().equals(configDTO.getExtensionId())) {
                e.update(configDTO);
                this.dateModified = new Date();
                break;
            }
        }
    }

    public void deleteExtension(Long extensionId) {
        Assert.notNull(extensionId, "extension id不可为空");
        Iterator<ExtensionEntity> iter = extensions.iterator();
        while (iter.hasNext()) {
            ExtensionEntity e = iter.next();
            if (e.getId().equals(extensionId)) {
                iter.remove();
                this.dateModified = new Date();
                break;
            }
        }
    }

    public void addExtension(SpiConfigDTO config) {
        ExtensionEntity extension = ExtensionEntity.create(config);
        multiDefaultNotAllowed(extension);
        extension.setSpi(this);
        extensions.add(extension);
        this.dateModified = new Date();
    }

    private void multiDefaultNotAllowed(ExtensionEntity extension) {
        if (extensions.stream().anyMatch(e -> e.getIsDefault() == 1) && extension.getIsDefault() == 1) {
            throw BusinessException.fail("不可设置多个默认实现");
        }
    }
}
