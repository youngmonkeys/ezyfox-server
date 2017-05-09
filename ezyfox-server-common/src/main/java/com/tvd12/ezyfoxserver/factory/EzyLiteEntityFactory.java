package com.tvd12.ezyfoxserver.factory;

public final class EzyLiteEntityFactory {

	private static final EzyEntityBuilderCreator CREATOR 
			= new EzySimpleLiteEntityBuilderCreator();
	
	private EzyLiteEntityFactory() {
	}
	
	public static <T> T create(Class<T> productType) {
		return CREATOR.create(productType);
	}
	
}