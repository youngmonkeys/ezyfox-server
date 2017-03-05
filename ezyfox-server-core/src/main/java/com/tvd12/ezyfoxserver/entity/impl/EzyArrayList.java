package com.tvd12.ezyfoxserver.entity.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.transformer.EzyInputTransformer;
import com.tvd12.ezyfoxserver.transformer.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyArrayList implements EzyArray {
	private static final long serialVersionUID = 5952111146742741007L;
	
	private List<Object> list = new ArrayList<>();
	
	@Setter
	protected transient EzyInputTransformer inputTransformer;
	@Setter
	protected transient EzyOutputTransformer outputTransformer;

	@Override
	public <T> T get(int index) {
		return (T)list.get(index);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#get(int, java.lang.Class)
	 */
	@Override
	public <T> T get(final int index, final Class<T> type) {
		return (T) transformOutput(list.get(index), type);
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
		list.addAll(items);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#size()
	 */
	@Override
	public int size() {
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#set(int, java.lang.Object)
	 */
	@Override
	public <T> T set(int index, Object item) {
		return (T) list.set(index, transformInput(item));
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#remove(int)
	 */
	@Override
	public <T> T remove(int index) {
		return (T) list.remove(index);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#forEach(java.util.function.Consumer)
	 */
	@Override
	public void forEach(Consumer<Object> action) {
		list.forEach(action);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#iterator()
	 */
	@Override
	public Iterator<Object> iterator() {
		return list.iterator();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#toList()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List toList() {
		return list;
	}
	
	/**
	 * add an item to the list
	 * 
	 * @param item the item
	 * @return add successful or not
	 */
	protected boolean add(Object item) {
		if(item == null)
			return list.add(item);
		return list.add(transformInput(item));
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
	
	@Override
	public String toString() {
		return list.toString();
	}

}
