package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyDisconnectUser extends EzyCommand<Boolean> {

	EzyDisconnectUser user(EzyUser user);
	
	EzyDisconnectUser session(EzySession session);
	
	EzyDisconnectUser reason(EzyConstant reason);
	
	EzyDisconnectUser fireClientEvent(boolean value);
	
	EzyDisconnectUser fireServerEvent(boolean value);
	
}
