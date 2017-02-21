package com.tvd12.ezyfoxserver.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyFoxArray;
import com.tvd12.ezyfoxserver.transformer.EzyFoxInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyFoxOutputTransformer;

@SuppressWarnings("unchecked")
public class EzyFoxArrayList extends ArrayList<Object> implements EzyFoxArray {
	private static final long serialVersionUID = 5952111146742741007L;
	
	private EzyFoxInputTransformer inputTransformer;
	private EzyFoxOutputTransformer outputTransformer;

	@Override
	public <T> T get(final int index, final Class<T> type) {
		return (T) transformOutput(super.get(index), type);
	}

	@Override
	public void add(final Object... values) {
		this.add(Lists.newArrayList(values));
	}

	@Override
	public void add(final Collection<Object> values) {
		super.addAll(values);
	}
	
	@Override
	public boolean add(Object e) {
		if(e == null)
			return super.add(e);
		return super.add(transformInput(e));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List toList() {
		return this;
	}
	
	protected Object transformInput(final Object e) {
		return inputTransformer.transform(e);
	}
	
	@SuppressWarnings("rawtypes")
	private Object transformOutput(final Object output, final Class type) {
		return outputTransformer.transform(output, type);
	}
	
}
