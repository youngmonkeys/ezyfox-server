package com.tvd12.ezyfoxserver.factory;

public final class EzyEntityFactory {

	private static final EzyEntityBuilderCreator CREATOR 
			= new EzySimpleEntityBuilderCreator();
	
	private EzyEntityFactory() {
		// do nothing
	}
	
	public static <T> T create(Class<T> productType) {
		return CREATOR.create(productType);
	}
	
}
