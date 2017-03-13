package com.tvd12.ezyfoxserver.factory;

import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfoxserver.factory.impl.EzyBuilderFactoryImpl;

public abstract class EzyFactory {

	private static final List<EzyCreator> CREATORS;
	
	static {
		CREATORS = newCreators();
	}
	
	private EzyFactory() {
		// do nothing
	}
	
	public static <T> T create(Class<T> productType) {
		for(EzyCreator creator : CREATORS)
			if(creator.creatable(productType))
				return creator.create(productType);
		throw new IllegalArgumentException("has no creator for " + productType);
	}
	
	private static final List<EzyCreator> newCreators() {
		List<EzyCreator> answer = new ArrayList<>();
		answer.add(new EzyBuilderFactoryImpl());
		return answer;
	}
	
}
