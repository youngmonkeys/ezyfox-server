package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyUserEvent extends EzyEvent {

    EzyUser getUser();

}
