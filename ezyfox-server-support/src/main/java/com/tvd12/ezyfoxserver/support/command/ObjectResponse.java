package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfoxserver.support.command.ObjectResponse;
import com.tvd12.ezyfoxserver.support.command.Response;

public interface ObjectResponse extends Response<ObjectResponse> {
	
	ObjectResponse param(Object key, Object value);
	
	
}
