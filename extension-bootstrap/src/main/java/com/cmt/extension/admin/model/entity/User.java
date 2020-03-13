package com.cmt.extension.admin.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author tuzhenxian
 * @date 19-10-25
 */

@Entity
@Table(name = "t_user")
@Data
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String userMobile;
    private String role;
    /**
     *已授权应用“,”分隔 不在其中的app只有查询权限
     */
    private String authorizedApps;
    //general columns
    private Date dateCreate;
    private Date dateModified;
    private String creator;
    private String modifier;
    /**
     * 是否有效 可进行软删除
     */
    private int sysFlag;
}