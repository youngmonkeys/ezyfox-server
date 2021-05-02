package com.tvd12.ezyfoxserver.event;

import java.util.Map;

import com.tvd12.ezyfox.entity.EzyData;

public interface EzyUserLoginEvent extends EzySessionEvent {

    String getZoneName();
    
	EzyData getOutput();
	
	String getUsername();
	
	String getPassword();
	
	<T extends EzyData> T getData();
	
	Map<Object, Object> getUserProperties();
	
	boolean isStreamingEnable();

	void setOutput(EzyData output);
	
	void setUsername(String username);
	
	void setPassword(String password);
	
	void setStreamingEnable(boolean enable);
	
	void setUserProperty(Object key, Object value);
	
	void setUserProperties(Map<Object, Object> properties);
	
}
