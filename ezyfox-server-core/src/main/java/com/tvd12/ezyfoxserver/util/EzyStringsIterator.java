package com.tvd12.ezyfoxserver.util;

public class EzyStringsIterator extends EzyArrayIterator<String> {

	private String[] array;
	
	public EzyStringsIterator(String[] array) {
		this.array = array;
	}
	
	public static EzyStringsIterator wrap(String[] array) {
		return new EzyStringsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected String getItem(int index) {
		return array[index];
	}

	
	
}
