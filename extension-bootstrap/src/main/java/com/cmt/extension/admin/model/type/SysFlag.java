package com.cmt.extension.admin.model.type;

import lombok.Getter;

/**
 * @author tuzhenxian
 * @date 19-10-28
 */
@Getter
public enum SysFlag {
    VALID(1),INVALID(0);
    private int code;

    SysFlag(int code) {
        this.code = code;
    }
}
