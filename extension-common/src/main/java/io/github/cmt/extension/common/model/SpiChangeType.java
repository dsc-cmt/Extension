package io.github.cmt.extension.common.model;

import lombok.Getter;

/**
 * @author tuzhenxian
 * @date 19-10-16
 */
@Getter
public enum SpiChangeType {
    INIT,
    ADDED,
    DELETED,
    MODIFIED;
}
