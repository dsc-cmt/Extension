package com.cmt.extension.core.configcenter.model;

import com.ctrip.framework.apollo.enums.PropertyChangeType;

import java.util.Objects;
import lombok.Getter;

/**
 * @author tuzhenxian
 * @date 19-10-16
 */
@Getter
public enum SpiChangeType {
    INIT(null),
    ADDED(PropertyChangeType.ADDED),
    DELETED(PropertyChangeType.DELETED),
    MODIFIED(PropertyChangeType.MODIFIED);

    SpiChangeType(PropertyChangeType apolloType) {
        this.apolloType = apolloType;
    }

    private PropertyChangeType apolloType;

    public static SpiChangeType matchType(PropertyChangeType apolloType){
        for(SpiChangeType type:SpiChangeType.values()){
            if (Objects.equals(apolloType,type.getApolloType())){
                return type;
            }
        }
        return null;
    }
}
