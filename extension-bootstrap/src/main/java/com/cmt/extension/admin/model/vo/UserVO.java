package com.cmt.extension.admin.model.vo;

import com.cmt.extension.admin.model.entity.User;

import java.util.Date;

import lombok.Data;

/**
 * @author tuzhenxian
 * @date 19-10-28
 */
@Data
public class UserVO {
    private Long id;
    private String userName;
    private String userMobile;
    private String role;
    /**
     * 已授权应用
     */
    private String authorizedApps;
    private String password;
    private Date dateCreate;
    private Date dateModified;
    private String creator;
    private String modifier;
    private String modifierMobile;

    public User buildUser(int sysFlag) {
        User user = new User();
        user.setId(this.id);
        user.setUserName(this.userName);
        user.setUserMobile(this.userMobile);
        user.setRole(this.role);
        user.setPassword(this.password);
        user.setAuthorizedApps(this.authorizedApps);
        user.setDateCreate(this.dateCreate);
        user.setDateModified(this.dateModified);
        user.setCreator(this.creator);
        user.setModifier(this.modifier);
        user.setSysFlag(sysFlag);
        return user;
    }

    public static UserVO buildByUser(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUserName(user.getUserName());
        vo.setUserMobile(user.getUserMobile());
        vo.setRole(user.getRole());
        vo.setDateModified(user.getDateModified());
        vo.setDateCreate(user.getDateCreate());
        vo.setCreator(user.getCreator());
        vo.setModifier(user.getModifier());
        vo.setPassword(user.getPassword());
        vo.setAuthorizedApps(user.getAuthorizedApps());
        return vo;
    }
}
