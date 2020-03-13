package com.cmt.extension.admin.model.type;

import lombok.Getter;

/**
 * @author tuzhenxian
 * @date 19-10-28
 */
@Getter
public enum RoleType {
    ADMIN("admin"),
    VISITOR("visitor"),
    ;
    private String desc;

    RoleType(String desc) {
        this.desc = desc;
    }

}
