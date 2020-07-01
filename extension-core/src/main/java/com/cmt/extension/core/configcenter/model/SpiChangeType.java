package com.cmt.extension.core.configcenter.model;

import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * @author tuzhenxian
 * @date 19-10-16
 */
public enum SpiChangeType {
    /**
     * 初始化
     */
    INIT,
    /**
     * 新增
     */
    ADDED,
    /**
     * 删除
     */
    DELETED,
    /**
     * 修改
     */
    MODIFIED;

    public static SpiChangeType matchZooKeeperType(PathChildrenCacheEvent.Type type) {
        switch (type) {
            case CHILD_ADDED:
                return ADDED;
            case CHILD_UPDATED:
                return MODIFIED;
            case CHILD_REMOVED:
                return DELETED;
            default:
                break;
        }
        return null;
    }
}
