package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyResponse;

public interface EzyArrayResponse extends EzyResponse<EzyArrayResponse> {

	EzyArrayResponse param(Object value);
	
}
