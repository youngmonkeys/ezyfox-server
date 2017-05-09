package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyUserLoginEvent extends EzySessionEvent {

	EzyData getOutput();
	
	String getUsername();
	
	String getPassword();
	
	EzyArray getData();

	void setOutput(EzyData output);
	
	void setUsername(String username);
	
	void setPassword(String password);
	
}
