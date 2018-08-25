package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfoxserver.support.command.ArrayResponse;
import com.tvd12.ezyfoxserver.support.command.Response;

public interface ArrayResponse extends Response<ArrayResponse> {

	ArrayResponse param(Object value);
	
}
