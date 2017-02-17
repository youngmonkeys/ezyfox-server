package com.tvd12.ezyfoxserver.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyFoxArray;

@SuppressWarnings("unchecked")
public class EzyFoxArrayList extends ArrayList<Object> implements EzyFoxArray {
	private static final long serialVersionUID = 5952111146742741007L;

	@Override
	public <T> T get(int index, Class<T> type) {
		return null;
	}

	@Override
	public void add(Object... values) {
		this.add(Lists.newArrayList(values));
	}

	@Override
	public void add(Collection<Object> values) {
		super.addAll(values);
	}
	
	@Override
	public boolean add(Object e) {
		if(e != null)
			return super.add(transformInput(e));
		return super.add(e);
	}
	
	protected Object transformInput(Object e) {
		return e;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List toList() {
		return this;
	}

}
