package com.tvd12.ezyfoxserver.util;

import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;

@SuppressWarnings("rawtypes")
public final class EzyEntityObjects {

	private EzyEntityObjects() {
	}
	
	public static EzyObject newObject(Map map) {
		return EzyEntityFactory.create(EzyObjectBuilder.class)
				.append(map)
				.build();
	}
	
}
