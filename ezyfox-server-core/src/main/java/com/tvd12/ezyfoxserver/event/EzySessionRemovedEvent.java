package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzySessionRemovedEvent extends EzyUserSessionEvent {
    
    EzyConstant getReason();
    
}
