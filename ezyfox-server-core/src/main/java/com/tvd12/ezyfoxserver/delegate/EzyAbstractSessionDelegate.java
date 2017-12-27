package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyAbstractSessionDelegate 
        extends EzyLoggable implements EzySessionDelegate {

	@Override
	public void onSessionRemoved(EzyConstant reason) {
	}
	
	@Override
	public void onSessionLoggedIn(EzyUser user) {
	}
	
}
