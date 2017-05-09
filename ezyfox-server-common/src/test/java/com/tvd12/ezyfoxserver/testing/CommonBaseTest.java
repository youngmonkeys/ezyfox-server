package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyLiteEntityFactory;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;

public class CommonBaseTest extends BaseTest {

	protected EzyObjectBuilder newObjectBuilder() {
		return EzyEntityFactory.create(EzyObjectBuilder.class);
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyEntityFactory.create(EzyArrayBuilder.class);
	}
	
	protected EzyArrayBuilder newLiteArrayBuilder() {
		return EzyLiteEntityFactory.create(EzyArrayBuilder.class);
	}
	
}
