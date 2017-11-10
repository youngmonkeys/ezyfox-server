package com.tvd12.ezyfoxserver.util;

public class EzyLongsIterator extends EzyArrayIterator<Long> {

	private long[] array;
	
	public EzyLongsIterator(long[] array) {
		this.array = array;
	}
	
	public static EzyLongsIterator wrap(long[] array) {
		return new EzyLongsIterator(array);
	}
	
	@Override
	protected int getLength() {
		return array.length;
	}

	@Override
	protected Long getItem(int index) {
		return array[index];
	}

	
	
}
