package com.tvd12.ezyfoxserver.command;

import java.util.function.Predicate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;

public interface EzyBroadcastAppsEvent extends EzyBroadcastEvent {

    void fire(EzyConstant type, EzyEvent event, String username, boolean catchException);
    
    void fire(EzyConstant type, EzyEvent event, EzyUser user, boolean catchException);
    
    void fire(EzyConstant type, EzyEvent event, Predicate<EzyAppContext> filter, boolean catchException);
    
}
