package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionEvent extends EzyEvent {

    EzySession getSession();

}
