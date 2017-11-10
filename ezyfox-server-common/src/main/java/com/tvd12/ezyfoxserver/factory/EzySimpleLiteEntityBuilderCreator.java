package com.tvd12.ezyfoxserver.factory;

import com.tvd12.ezyfoxserver.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyLiteCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyLiteOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzySimpleLiteEntityBuilderCreator extends EzySimpleEntityBuilderCreator {

	private static final EzyOutputTransformer OUTPUT_TRANSFORMER 
			= new EzyLiteOutputTransformer();
	private static final EzyCollectionConverter COLLECTION_CONVERTER 
			= new EzyLiteCollectionConverter(OUTPUT_TRANSFORMER);

	@Override
	protected EzyOutputTransformer getOutputTransformer() {
		return OUTPUT_TRANSFORMER;
	}

	@Override
	protected EzyCollectionConverter getCollectionConverter() {
		return COLLECTION_CONVERTER;
	}

}
