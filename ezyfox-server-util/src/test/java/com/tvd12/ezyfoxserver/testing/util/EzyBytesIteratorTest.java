package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyShortsIterator;

public class EzyBytesIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new short[] {1, 2, 3};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyShortsIterator.wrap(new short[] {1, 2, 3});
	}

}
