package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;

public interface EzyHasSessionDelegate {

    /**
     * @param delegate the delegate
     */
    void setDelegate(EzySessionDelegate delegate);
    
    /**
     * @return the delegate
     */
    EzySessionDelegate getDelegate();
    
}
