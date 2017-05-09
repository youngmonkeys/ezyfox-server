package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;

public interface EzyHasUserRemoveDelegate {

    /**
     * @param delegate the delegate
     */
    void setRemoveDelegate(EzyUserRemoveDelegate delegate);
    
    /**
     * @return the delegate
     */
    EzyUserRemoveDelegate getRemoveDelegate();
    
}
