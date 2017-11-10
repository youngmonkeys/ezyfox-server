package com.tvd12.ezyfoxserver.context;

public interface EzyChildContext extends EzyContext {

    EzyServerContext getParent();
    
}
