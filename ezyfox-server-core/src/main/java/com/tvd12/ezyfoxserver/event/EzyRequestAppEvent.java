package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyRequestAppEvent extends EzyEvent {

	EzyUser getUser();
	
	<T extends EzyData> T getData();
	
}
