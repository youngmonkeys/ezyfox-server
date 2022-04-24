package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyData;

public interface EzyUserAddedEvent extends EzyUserSessionEvent {

    EzyData getLoginData();

}
