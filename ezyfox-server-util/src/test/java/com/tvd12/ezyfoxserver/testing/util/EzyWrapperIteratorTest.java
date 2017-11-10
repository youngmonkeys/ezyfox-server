package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyWrapperIterator;

public class EzyWrapperIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new String[] {"a", "b", "c"};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyWrapperIterator.wrap(new String[] {"a", "b", "c"});
	}

}
