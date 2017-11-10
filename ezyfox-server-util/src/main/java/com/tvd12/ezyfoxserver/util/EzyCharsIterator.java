package com.tvd12.ezyfoxserver.util;

public class EzyCharsIterator extends EzyArrayIterator<Character> {

	private char[] array;
	
	public EzyCharsIterator(char[] array) {
		this.array = array;
	}
	
	public static EzyCharsIterator wrap(char[] array) {
		return new EzyCharsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Character getItem(int index) {
		return array[index];
	}

	
	
}
