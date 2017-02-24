package com.tvd12.ezyfoxserver.factory.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.builder.impl.EzyArrayBuilderImpl;
import com.tvd12.ezyfoxserver.builder.impl.EzyObjectBuilderImpl;
import com.tvd12.ezyfoxserver.factory.EzyBuilderFactory;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

public class EzyBuilderFactoryImpl implements EzyBuilderFactory {

	@SuppressWarnings("rawtypes")
	private static final Map<Class, Supplier> SUPPLIERS;
	
	private static final EzyInputTransformer INPUT_TRANSFORMER;
	private static final EzyOutputTransformer OUTPUT_TRANSFORMER;
	
	static {
		INPUT_TRANSFORMER 	= new EzyInputTransformer();
		OUTPUT_TRANSFORMER 	= new EzyOutputTransformer();
		SUPPLIERS 			= defaultSuppliers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> productType) {
		return (T) getSuppliers().get(productType).get();
	}
	
	@Override
	public <T> boolean creatable(Class<T> productType) {
		return SUPPLIERS.containsKey(productType);
	}
	
	@SuppressWarnings("rawtypes")
	protected Map<Class, Supplier> getSuppliers() {
		return SUPPLIERS;
	}
	
	@SuppressWarnings("rawtypes")
	private static final Map<Class, Supplier> defaultSuppliers() {
		Map<Class, Supplier> answer = new HashMap<>();
		answer.put(EzyObjectBuilder.class, () -> {
			return new EzyObjectBuilderImpl.Creator()
					.inputTransformer(INPUT_TRANSFORMER)
					.outputTransformer(OUTPUT_TRANSFORMER)
					.create();
		});
		answer.put(EzyArrayBuilder.class, () -> {
			return new EzyArrayBuilderImpl.Creator()
				.inputTransformer(INPUT_TRANSFORMER)
				.outputTransformer(OUTPUT_TRANSFORMER)
				.create();
		});
		return answer;
	}

}
