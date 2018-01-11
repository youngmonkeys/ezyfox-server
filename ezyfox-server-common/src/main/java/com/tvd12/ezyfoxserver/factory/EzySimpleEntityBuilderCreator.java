package com.tvd12.ezyfoxserver.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.builder.EzySimpleArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzySimpleObjectBuilder;
import com.tvd12.ezyfoxserver.concurrent.EzyLazyInitializer;
import com.tvd12.ezyfoxserver.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzySimpleInputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleOutputTransformer;

public class EzySimpleEntityBuilderCreator implements EzyEntityBuilderCreator {

	private static final EzyInputTransformer INPUT_TRANSFORMER 
			= new EzySimpleInputTransformer();
	private static final EzyOutputTransformer OUTPUT_TRANSFORMER 
			= new EzySimpleOutputTransformer();
	private static final EzyCollectionConverter COLLECTION_CONVERTER 
			= new EzySimpleCollectionConverter();
	
	@SuppressWarnings("rawtypes")
	private EzyLazyInitializer<Map<Class, Supplier>> suppliers 
			= new EzyLazyInitializer<>(this::defaultSuppliers);
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> productType) {
		return (T) getSuppliers().get(productType).get();
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> getSuppliers() {
		return suppliers.get();
	}
	
	protected EzyInputTransformer getInputTransformer() {
		return INPUT_TRANSFORMER;
	}
	
	protected EzyOutputTransformer getOutputTransformer() {
		return OUTPUT_TRANSFORMER;
	}
	
	protected EzyCollectionConverter getCollectionConverter() {
		return COLLECTION_CONVERTER;
	}
	
	@SuppressWarnings("rawtypes")
	private final Map<Class, Supplier> defaultSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		answer.put(EzyObjectBuilder.class, () -> {
			return new EzySimpleObjectBuilder.Creator()
					.inputTransformer(getInputTransformer())
					.outputTransformer(getOutputTransformer())
					.create();
		});
		answer.put(EzyArrayBuilder.class, () -> {
			return new EzySimpleArrayBuilder.Creator()
				.inputTransformer(getInputTransformer())
				.outputTransformer(getOutputTransformer())
				.collectionConverter(getCollectionConverter())
				.create();
		});
		return answer;
	}

}
