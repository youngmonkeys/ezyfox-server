package com.tvd12.ezyfoxserver.builder.impl;

import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.entity.impl.EzyHashMap;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

public class EzyObjectBuilderImpl implements EzyObjectBuilder {

	protected EzyObject product;
	
	@Setter
	protected EzyInputTransformer inputTransformer;
	@Setter
	protected EzyOutputTransformer outputTransformer;
	
	public EzyObjectBuilderImpl() {
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
	@Override
	public EzyObjectBuilder append(Map<? extends Object, ? extends Object> map) {
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
	
}
