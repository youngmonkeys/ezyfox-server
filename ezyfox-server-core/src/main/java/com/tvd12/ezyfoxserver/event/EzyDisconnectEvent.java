package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyDisconnectEvent extends EzyEvent {

	EzyUser getUser();
	
	EzyConstant getReason();
	
}
