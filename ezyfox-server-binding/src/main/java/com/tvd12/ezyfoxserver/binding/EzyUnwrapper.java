package com.tvd12.ezyfoxserver.binding;

public interface EzyUnwrapper<I,O> {

	void unwrap(EzyUnmarshaller unmarshaller, I input, O output);
	
}
