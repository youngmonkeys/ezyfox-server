package com.tvd12.ezyfoxserver.codec.testing;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;

public class CodecBaseTest extends BaseTest {

	public EzyArrayBuilder newArrayBuilder() {
		return EzyEntityFactory.create(EzyArrayBuilder.class);
	}
	
	public EzyObjectBuilder newObjectBuilder() {
		return EzyEntityFactory.create(EzyObjectBuilder.class);
	}
	
}
