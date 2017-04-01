package com.tvd12.ezyfoxserver.util;

public class EzyFloatsIterator extends EzyArrayIterator<Float> {

	private float[] array;
	
	public EzyFloatsIterator(float[] array) {
		this.array = array;
	}
	
	public static EzyFloatsIterator wrap(float[] array) {
		return new EzyFloatsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Float getItem(int index) {
		return array[index];
	}

	
	
}
