package com.cmt.extension.admin.model.type;

import lombok.Getter;

/**
 * @author xieyong
 * @date 2020/3/26
 * @Description:
 */
@Getter
public enum LoginStatusEnum {
    OK("ok");
    private String desc;

    LoginStatusEnum(String desc) {
        this.desc = desc;
    }
}
