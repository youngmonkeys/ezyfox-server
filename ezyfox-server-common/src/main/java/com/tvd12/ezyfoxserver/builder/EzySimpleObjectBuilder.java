package com.tvd12.ezyfoxserver.builder;

import java.util.Map;

import com.tvd12.ezyfoxserver.entity.EzyHashMap;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzySimpleObjectBuilder 
		extends EzyHasInOutTransformer 
		implements EzyObjectBuilder {

	protected EzyObject product;
	
	public EzySimpleObjectBuilder(
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
	
	public static class Creator extends AbstractCreator<EzySimpleObjectBuilder, Creator> {
		@Override
		public EzySimpleObjectBuilder create() {
			return new EzySimpleObjectBuilder(inputTransformer, outputTransformer);
		}
	}
	
}
