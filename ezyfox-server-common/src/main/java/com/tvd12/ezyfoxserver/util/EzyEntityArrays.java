package com.tvd12.ezyfoxserver.util;

import java.util.List;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;

@SuppressWarnings("rawtypes")
public final class EzyEntityArrays {

	private EzyEntityArrays() {
	}
	
	public static EzyArray newArray() {
		return EzyEntityFactory.create(EzyArrayBuilder.class)
				.build();
	}
	
	public static EzyArray newArray(List list) {
		return EzyEntityFactory.create(EzyArrayBuilder.class)
				.append(list)
				.build();
	}
	
	public static EzyArray newArray(Object... args) {
		return EzyEntityFactory.create(EzyArrayBuilder.class)
				.append(args)
				.build();
	}
	
}
