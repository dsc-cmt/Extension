package io.github.cmt.extension.admin.model;

import lombok.NoArgsConstructor;

/**
 * @author tuzhenxian
 * @date 19-10-11
 */
@NoArgsConstructor
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public static BusinessException fail(String msg) {
        return new BusinessException(msg);
    }
}
