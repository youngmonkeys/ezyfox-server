package com.tvd12.ezyfoxserver.command;

import java.util.function.Predicate;

import com.tvd12.ezyfoxserver.context.EzyAppContext;

public interface EzyFireAppEvent extends EzyFireEvent {

    EzyFireAppEvent filter(Predicate<EzyAppContext> filter);
    
}
