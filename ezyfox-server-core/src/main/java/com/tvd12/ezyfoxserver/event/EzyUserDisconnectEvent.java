package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

public interface EzyUserDisconnectEvent extends EzyUserEvent {

    EzyConstant getReason();
	
}
