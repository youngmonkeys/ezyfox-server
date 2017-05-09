package com.tvd12.ezyfoxserver.testing.entity;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;

public class EzyEntityTest extends BaseTest {

	protected EzyArrayBuilder newArrayBuilder() {
		return EzyEntityFactory.create(EzyArrayBuilder.class);
	}
	
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyEntityFactory.create(EzyObjectBuilder.class);
	}
	
}
