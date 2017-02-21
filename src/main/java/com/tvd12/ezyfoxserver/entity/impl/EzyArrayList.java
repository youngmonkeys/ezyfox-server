package com.tvd12.ezyfoxserver.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

@SuppressWarnings("unchecked")
public class EzyArrayList extends ArrayList<Object> implements EzyArray {
	private static final long serialVersionUID = 5952111146742741007L;
	
	private EzyInputTransformer inputTransformer;
	private EzyOutputTransformer outputTransformer;

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
