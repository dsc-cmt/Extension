package com.cmt.extension.admin.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.cmt.extension.admin.model.BusinessException;
import com.cmt.extension.core.configcenter.model.SpiConfigDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 应用
 *
 * @author yonghuang
 */
@Entity
@Table(name = "extension_app")
@Data
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用名
     **/
    private String appName;

    /**
     * 创建人ID
     **/
    private Long creatorId;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spi> spis = new ArrayList<>();
    @Version
    private Integer version;

    public App(String appName, Long creatorId) {
        this.appName = appName;
        this.creatorId = creatorId;
    }

    public void addSpi(String spiInterface, String desc) {
        List<String> spiInterfaces = spis.stream().map(Spi::getSpiInterface).collect(Collectors.toList());
        if (spiInterfaces.contains(spiInterface)) {
            return;
        }
        Spi newSpi = Spi.create(spiInterface, desc);
        newSpi.setApp(this);
        spis.add(newSpi);
    }

    public void addExtension(SpiConfigDTO config) {
        Spi spi = getSpi(config.getSpiInterface());
        spi.addExtension(config);
        this.dateModified = new Date();
    }

    public void updateExtension(SpiConfigDTO config) {
        Spi spi = getSpi(config.getSpiInterface());
        spi.updateExtension(config);
        this.dateModified = new Date();
    }

    public void deleteExtension(SpiConfigDTO configDTO) {
        Spi spi = getSpi(configDTO.getSpiInterface());
        spi.deleteExtension(configDTO.getExtensionId());
        this.dateModified = new Date();
    }

    private Spi getSpi(String spiInterface) {
        for (Spi spi : spis) {
            if (spi.getSpiInterface().equals(spiInterface)) {
                return spi;
            }
        }
        throw BusinessException.fail("扩展点不存在");
    }
}
