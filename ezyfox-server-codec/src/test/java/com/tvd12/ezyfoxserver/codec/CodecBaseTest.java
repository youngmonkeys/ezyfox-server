package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

public class CodecBaseTest {

	public EzyArrayBuilder newArrayBuilder() {
		return EzyFactory.create(EzyArrayBuilder.class);
	}
	
	public EzyObjectBuilder newObjectBuilder() {
		return EzyFactory.create(EzyObjectBuilder.class);
	}
	
}
