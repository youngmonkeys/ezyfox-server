package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyBoolsIterator;

public class EzyBoolsIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new boolean[] {true, false, true};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyBoolsIterator.wrap(new boolean[] {true, false, true});
	}

}
