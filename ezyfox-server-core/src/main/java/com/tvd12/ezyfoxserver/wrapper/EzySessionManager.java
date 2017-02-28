package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionManager {

	EzySession borrowSession();
	
	void returnSession(final EzySession session);
	
}
