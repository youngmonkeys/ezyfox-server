package com.tvd12.ezyfoxserver.util;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyLiteEntityFactory;

public class EzyLiteEntityBuilders extends EzyLoggable {
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyLiteEntityFactory.create(EzyArrayBuilder.class);
	}

	protected EzyObjectBuilder newObjectBuilder() {
		return EzyLiteEntityFactory.create(EzyObjectBuilder.class);
	}
	
}
