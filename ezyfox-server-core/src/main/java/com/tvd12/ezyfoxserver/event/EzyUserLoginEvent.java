package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyData;

public interface EzyUserLoginEvent extends EzySessionEvent {

    String getZoneName();
    
	EzyData getOutput();
	
	String getUsername();
	
	String getPassword();
	
	EzyData getData();
	
	boolean isStreamingEnable();

	void setOutput(EzyData output);
	
	void setUsername(String username);
	
	void setPassword(String password);
	
	void setStreamingEnable(boolean enable);
	
}
