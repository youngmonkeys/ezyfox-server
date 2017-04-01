package com.tvd12.ezyfoxserver.factory;

import com.tvd12.ezyfoxserver.factory.impl.EzyBuilderFactoryImpl;
import com.tvd12.ezyfoxserver.io.EzyLiteOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public abstract class EzyEntityFactory {

	private static final BuilderFactory BUILDER_FACTORY;
	
	static {
		BUILDER_FACTORY = new BuilderFactory();
	}
	
	private EzyEntityFactory() {
	}
	
	public static <T> T create(Class<T> productType) {
		if(BUILDER_FACTORY.creatable(productType))
			return BUILDER_FACTORY.create(productType);
		throw new IllegalArgumentException("has no creator for " + productType);
	}
	
}

class BuilderFactory extends EzyBuilderFactoryImpl {
	
	private static final EzyOutputTransformer OUTPUT_TRANSFORMER;
	
	static {
		OUTPUT_TRANSFORMER 	= new EzyLiteOutputTransformer();
	}
	
	@Override
	protected EzyOutputTransformer getOutputTransformer() {
		return OUTPUT_TRANSFORMER;
	}
	
}
