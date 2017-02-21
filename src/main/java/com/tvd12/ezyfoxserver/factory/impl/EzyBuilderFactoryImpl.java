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
			EzyObjectBuilderImpl builder = new EzyObjectBuilderImpl();
			builder.setInputTransformer(INPUT_TRANSFORMER);
			builder.setOutputTransformer(OUTPUT_TRANSFORMER);
			return builder;
		});
		answer.put(EzyArrayBuilder.class, () -> {
			EzyArrayBuilderImpl builder = new EzyArrayBuilderImpl();
			builder.setInputTransformer(INPUT_TRANSFORMER);
			builder.setOutputTransformer(OUTPUT_TRANSFORMER);
			return builder;
		});
		return answer;
	}

}
