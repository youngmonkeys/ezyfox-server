package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyIntsIterator;

public class EzyIntsIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new int[] {1, 2, 3};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyIntsIterator.wrap(new int[] {1, 2, 3});
	}

}
