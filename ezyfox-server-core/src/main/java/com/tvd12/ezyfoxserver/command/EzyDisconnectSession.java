package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyDisconnectSession extends EzyCommand<Boolean> {

	EzyDisconnectSession session(EzySession session);
	
	EzyDisconnectSession reason(EzyConstant reason);
	
	EzyDisconnectSession fireClientEvent(boolean value);
	
}
