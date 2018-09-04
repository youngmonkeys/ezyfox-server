package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyCloseSession {
    
    void close(EzySession session, EzyConstant reason);
	
}
