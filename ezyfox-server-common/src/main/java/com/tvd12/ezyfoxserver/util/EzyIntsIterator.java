package com.tvd12.ezyfoxserver.util;

public class EzyIntsIterator extends EzyArrayIterator<Integer> {

	private int[] array;
	
	public EzyIntsIterator(int[] array) {
		this.array = array;
	}
	
	public static EzyIntsIterator wrap(int[] array) {
		return new EzyIntsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Integer getItem(int index) {
		return array[index];
	}

	
	
}
