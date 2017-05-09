package com.tvd12.ezyfoxserver.util;

public class EzyShortsIterator extends EzyArrayIterator<Short> {

	private short[] array;
	
	public EzyShortsIterator(short[] array) {
		this.array = array;
	}
	
	public static EzyShortsIterator wrap(short[] array) {
		return new EzyShortsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Short getItem(int index) {
		return array[index];
	}

	
	
}
