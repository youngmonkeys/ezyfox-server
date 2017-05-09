package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyFloatsIterator;

public class EzyFloatsIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new float[] {1, 2, 3};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyFloatsIterator.wrap(new float[] {1, 2, 3});
	}

}
