package com.tvd12.ezyfoxserver.builder.impl;

import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.impl.EzyHashMap;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzyObjectBuilderImpl 
		extends EzyInOutTransformerNeeder 
		implements EzyObjectBuilder {

	protected EzyObject product;
	
	public EzyObjectBuilderImpl(
			EzyInputTransformer inputTransformer, 
			EzyOutputTransformer outputTransformer) {
		super(inputTransformer, outputTransformer);
		this.product = newProduct();
	}
	
	protected EzyHashMap newProduct() {
		EzyHashMap answer = new EzyHashMap();
		answer.setInputTransformer(inputTransformer);
		answer.setOutputTransformer(outputTransformer);
		return answer;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyObjectBuilder#append(java.lang.Object, java.lang.Object)
	 */
	@Override
	public EzyObjectBuilder append(Object key, Object value) {
		this.product.put(key, value);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyObjectBuilder#append(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public EzyObjectBuilder append(Map map) {
		this.product.putAll(map);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyBuilder#build()
	 */
	@Override
	public EzyObject build() {
		return this.product;
	}
	
	public static class Creator extends AbstractCreator<EzyObjectBuilderImpl> {
		@Override
		public EzyObjectBuilderImpl create() {
			return new EzyObjectBuilderImpl(inputTransformer, outputTransformer);
		}
	}
	
}
