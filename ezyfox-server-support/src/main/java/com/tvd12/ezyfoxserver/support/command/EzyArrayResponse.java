package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;

public interface EzyArrayResponse extends EzyResponse<EzyArrayResponse> {

	EzyArrayResponse param(Object value);
	
	EzyArrayResponse params(Object... values);
	
	EzyArrayResponse params(Iterable<?> values);
}
