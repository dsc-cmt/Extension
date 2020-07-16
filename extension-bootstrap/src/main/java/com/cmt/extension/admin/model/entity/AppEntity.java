package com.cmt.extension.admin.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

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
public class AppEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 应用名 **/
    private String appName;

    /** 创建人ID **/
    private Long creatorId;

    @CreatedDate
    private Date dateCreate;
    @LastModifiedDate
    private Date dateModified;

    public AppEntity(String appName, Long creatorId) {
        this.appName = appName;
        this.creatorId = creatorId;
    }
}
