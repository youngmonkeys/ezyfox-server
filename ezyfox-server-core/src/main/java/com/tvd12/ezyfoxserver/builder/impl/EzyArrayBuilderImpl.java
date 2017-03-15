package com.tvd12.ezyfoxserver.builder.impl;

import java.util.Collection;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.impl.EzyArrayList;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzyArrayBuilderImpl
		extends EzyInOutTransformerNeeder
		implements EzyArrayBuilder {

	protected EzyArray product;
	
	public EzyArrayBuilderImpl(
			EzyInputTransformer inputTransformer, 
			EzyOutputTransformer outputTransformer) {
		super(inputTransformer, outputTransformer);
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
	
	public static class Creator extends AbstractCreator<EzyArrayBuilderImpl> {
		@Override
		public EzyArrayBuilderImpl create() {
			return new EzyArrayBuilderImpl(inputTransformer, outputTransformer);
		}
	}

}
