package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyCloseSession extends EzyCommand<Boolean> {

	EzyCloseSession session(EzySession session);
	
	EzyCloseSession reason(EzyConstant reason);
	
}
