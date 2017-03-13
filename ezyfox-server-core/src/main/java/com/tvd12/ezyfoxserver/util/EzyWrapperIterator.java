package com.tvd12.ezyfoxserver.util;

public class EzyWrapperIterator<E> extends EzyArrayIterator<E> {

	private final E[] array;
	
	public EzyWrapperIterator(E[] array) {
		this.array = array;
	}
	
	public static <E> EzyWrapperIterator<E> wrap(E[] array) {
		return new EzyWrapperIterator<>(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}
	
	@Override
	protected E getItem(int index) {
		return array[index];
	}

}
