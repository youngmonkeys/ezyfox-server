package com.tvd12.ezyfoxserver.builder.impl;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.impl.EzyArrayList;
import com.tvd12.ezyfoxserver.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

public class EzyArrayBuilderImpl
		extends EzyInOutTransformerNeeder
		implements EzyArrayBuilder {

	protected EzyArray product;
	protected EzyCollectionConverter collectionConverter;
	
	public EzyArrayBuilderImpl(
			EzyInputTransformer inputTransformer, 
			EzyOutputTransformer outputTransformer,
			EzyCollectionConverter collectionConverter) {
		super(inputTransformer, outputTransformer);
		this.collectionConverter = collectionConverter;
		this.product = newProduct();
	}
	
	protected EzyArray newProduct() {
		EzyArrayList answer = new EzyArrayList();
		answer.setInputTransformer(inputTransformer);
		answer.setOutputTransformer(outputTransformer);
		answer.setCollectionConverter(collectionConverter);
		return answer;
	}
	
	@Override
	public <T> EzyArrayBuilder append(T item) {
		this.product.add(item);
		return this;
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
	@SuppressWarnings("rawtypes")
	@Override
	public EzyArrayBuilder append(Iterable items) {
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
	
	public static class Creator extends AbstractCreator<EzyArrayBuilderImpl, Creator> {
		
		protected EzyCollectionConverter collectionConverter;
		
		public Creator collectionConverter(EzyCollectionConverter converter) {
			this.collectionConverter = converter;
			return this;
		}
		
		@Override
		public EzyArrayBuilderImpl create() {
			return new EzyArrayBuilderImpl(
					inputTransformer, outputTransformer, collectionConverter);
		}
	}

}
