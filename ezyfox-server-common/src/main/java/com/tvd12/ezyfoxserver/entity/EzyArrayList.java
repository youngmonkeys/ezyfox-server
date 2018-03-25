package com.tvd12.ezyfoxserver.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.tvd12.ezyfoxserver.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyInputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;

import lombok.Setter;

@SuppressWarnings("unchecked")
public class EzyArrayList implements EzyArray {
	private static final long serialVersionUID = 5952111146742741007L;
	
	protected ArrayList<Object> list = new ArrayList<>();
	
	@Setter
	protected transient EzyInputTransformer inputTransformer;
	@Setter
	protected transient EzyOutputTransformer outputTransformer;
	@Setter
	protected transient EzyCollectionConverter collectionConverter;
	
	public EzyArrayList() {
	}
	
	@SuppressWarnings("rawtypes")
	public EzyArrayList(Collection items) {
		list.addAll(items);
	}

	@Override
	public <T> T get(int index) {
		return (T)list.get(index);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#get(int, java.lang.Class)
	 */
	@Override
	public <T> T get(int index, Class<T> type) {
		return (T) getValue(index, type);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#getValue(int, java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getValue(int index, Class type) {
		return transformOutput(list.get(index), type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#isNotNullIndex(int)
	 */
	@Override
	public boolean isNotNullValue(int index) {
		return index < size() ? list.get(index) != null : false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#sub(int, int)
	 */
	@Override
	public EzyArray sub(int fromIndex, int toIndex) {
		return new EzyArrayList(list.subList(fromIndex, toIndex));
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#add(java.lang.Object[])
	 */
	@Override
	public void add(Object... items) {
		add(Arrays.asList(items));
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#add(java.util.Collection)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void add(Collection items) {
		items.forEach(this::add);
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
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return list.isEmpty();
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
		return new ArrayList<>(list);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#toList(java.lang.Class)
	 */
	@Override
	public <T> List<T> toList(Class<T> type) {
		return toList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyRoArray#toArray(java.lang.Class)
	 */
	@Override
	public <T,A> A toArray(Class<T> type) {
		return getCollectionConverter().toArray(list, type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object clone() throws CloneNotSupportedException {
		EzyArrayList clone = new EzyArrayList((Collection) list.clone());
		clone.setInputTransformer(inputTransformer);
		clone.setOutputTransformer(outputTransformer);
		clone.setCollectionConverter(getCollectionConverter());
		return clone;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.entity.EzyArray#duplicate()
	 */
	@Override
	public EzyArray duplicate() {
		try {
			return (EzyArray) clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}
	
	/**
	 * add an item to the list
	 * 
	 * @param item the item
	 * @return add successful or not
	 */
	@Override
	public void add(Object item) {
		list.add(transformInput(item));
	}
	
	/**
	 * Transform input value
	 * 
	 * @param input the input value
	 * @return the transformed value
	 */
	protected Object transformInput(Object input) {
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
	private Object transformOutput(Object output, Class type) {
		return outputTransformer.transform(output, type);
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return list.toString();
	}
	
	protected EzyCollectionConverter getCollectionConverter() {
		return collectionConverter;
	}

}
