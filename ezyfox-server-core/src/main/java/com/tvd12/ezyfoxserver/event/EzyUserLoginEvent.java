package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyUserLoginEvent extends EzyEvent {

	Object getOutput();
	
	String getUsername();
	
	String getPassword();
	
	EzySession getSession();
	
	<T> T getData();

	void setOutput(Object output);
	
	void setUsername(String username);
	
	void setPassword(String password);
	
}
