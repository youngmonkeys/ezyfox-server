package com.tvd12.ezyfoxserver.factory.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.builder.impl.EzyArrayBuilderImpl;
import com.tvd12.ezyfoxserver.builder.impl.EzyObjectBuilderImpl;
import com.tvd12.ezyfoxserver.concurrent.EzyLazyInitializer;
import com.tvd12.ezyfoxserver.factory.EzyBuilderFactory;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleInputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleOutputTransformer;

public class EzyBuilderFactoryImpl implements EzyBuilderFactory {

	@SuppressWarnings("rawtypes")
	private EzyLazyInitializer<Map<Class, Supplier>> suppliers;
	
	private static final EzyInputTransformer INPUT_TRANSFORMER;
	private static final EzyOutputTransformer OUTPUT_TRANSFORMER;
	
	{
		suppliers = new EzyLazyInitializer<>(this::defaultSuppliers);
	}
	
	static {
		INPUT_TRANSFORMER 	= new EzySimpleInputTransformer();
		OUTPUT_TRANSFORMER 	= new EzySimpleOutputTransformer();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T create(Class<T> productType) {
		return (T) getSuppliers().get(productType).get();
	}
	
	@Override
	public <T> boolean creatable(Class<T> productType) {
		return getSuppliers().containsKey(productType);
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
	
	@SuppressWarnings("rawtypes")
	private final Map<Class, Supplier> defaultSuppliers() {
		Map<Class, Supplier> answer = new ConcurrentHashMap<>();
		answer.put(EzyObjectBuilder.class, () -> {
			return new EzyObjectBuilderImpl.Creator()
					.inputTransformer(getInputTransformer())
					.outputTransformer(getOutputTransformer())
					.create();
		});
		answer.put(EzyArrayBuilder.class, () -> {
			return new EzyArrayBuilderImpl.Creator()
				.inputTransformer(getInputTransformer())
				.outputTransformer(getOutputTransformer())
				.create();
		});
		return answer;
	}

}
