package com.tvd12.ezyfoxserver.util;

public class EzyDoublesIterator extends EzyArrayIterator<Double> {

	private double[] array;
	
	public EzyDoublesIterator(double[] array) {
		this.array = array;
	}
	
	public static EzyDoublesIterator wrap(double[] array) {
		return new EzyDoublesIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Double getItem(int index) {
		return array[index];
	}

	
	
}
