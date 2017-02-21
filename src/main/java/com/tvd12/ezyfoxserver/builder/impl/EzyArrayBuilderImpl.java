package com.tvd12.ezyfoxserver.builder.impl;

import java.util.Collection;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.impl.EzyArrayList;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

public class EzyArrayBuilderImpl implements EzyArrayBuilder {

	protected EzyArray product;
	
	@Setter
	protected EzyInputTransformer inputTransformer;
	@Setter
	protected EzyOutputTransformer outputTransformer;
	
	public EzyArrayBuilderImpl() {
		this.product = newProduct();
	}
	
	protected EzyArray newProduct() {
		EzyArrayList answer = new EzyArrayList();
		answer.setInputTransformer(inputTransformer);
		answer.setOutputTransformer(outputTransformer);
		return answer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyArrayBuilder#append(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> EzyArrayBuilder append(T... items) {
		this.product.add(items);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyArrayBuilder#append(java.util.Collection)
	 */
	@Override
	public EzyArrayBuilder append(Collection<? extends Object> items) {
		this.product.add(items);
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyBuilder#build()
	 */
	@Override
	public EzyArray build() {
		return product;
	}

}
