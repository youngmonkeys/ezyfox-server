package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyFactory;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyBaseResponse extends EzyLoggable {

	protected EzyArrayBuilder newArrayBuilder() {
		return EzyFactory.create(EzyArrayBuilder.class);
	}
	
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyFactory.create(EzyObjectBuilder.class);
	}
	
}
