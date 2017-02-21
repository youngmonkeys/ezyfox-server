package com.tvd12.ezyfoxserver.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyArrayList extends ArrayList<Object> implements EzyArray {
	private static final long serialVersionUID = 5952111146742741007L;
	
	@Setter
	protected EzyInputTransformer inputTransformer;
	@Setter
	protected EzyOutputTransformer outputTransformer;

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#get(int, java.lang.Class)
	 */
	@Override
	public <T> T get(final int index, final Class<T> type) {
		return (T) transformOutput(super.get(index), type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#add(java.lang.Object[])
	 */
	@Override
	public <T> void add(final T... items) {
		this.add(Lists.newArrayList(items));
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#add(java.util.Collection)
	 */
	@Override
	public void add(final Collection<? extends Object> items) {
		super.addAll(items);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(Object item) {
		if(item == null)
			return super.add(item);
		return super.add(transformInput(item));
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#toList()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List toList() {
		return this;
	}
	
	/**
	 * Transform input value
	 * 
	 * @param input the input value
	 * @return the transformed value
	 */
	protected Object transformInput(final Object input) {
		return inputTransformer.transform(input);
	}

	/**
	 * Transform output value
	 * 
	 * @param output the output value
	 * @param type the output type
	 * @return the transformed value
	 */
	@SuppressWarnings("rawtypes")
	private Object transformOutput(final Object output, final Class type) {
		return outputTransformer.transform(output, type);
	}
	
}
