package com.cmt.extension.admin.model.entity;

import com.cmt.extension.core.configcenter.model.SpiConfigDTO;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
public class SpiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 应用id **/
    private Long appId;

    /** spi接口 **/
    private String spiInterface;

    /** 描述 **/
    private String description;

    /** SPI扩展点实现 **/
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "spi_id", referencedColumnName = "id")
    private List<ExtensionEntity> extensionList;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;

    public static SpiEntity create(SpiConfigDTO configDTO) {
        SpiEntity spiEntity = new SpiEntity();
        spiEntity.setAppId(configDTO.getAppId());
        spiEntity.setSpiInterface(configDTO.getSpiInterface());

        List<ExtensionEntity> extensionList = new ArrayList<>();
        extensionList.add(ExtensionEntity.create(configDTO));
        spiEntity.setExtensionList(extensionList);

        return spiEntity;
    }

    public void updateExtension(SpiConfigDTO configDTO) {
        for (ExtensionEntity e : extensionList) {
            if (e.getId().equals(configDTO.getExtensionId())) {
                e.update(configDTO);
                break;
            }
        }
    }

    public void deleteExtension(Long extensionId) {
        Iterator<ExtensionEntity> iter = extensionList.iterator();
        while (iter.hasNext()) {
            ExtensionEntity e = iter.next();
            if (e.getId().equals(extensionId)) {
                iter.remove();
                break;
            }
        }
    }
}
