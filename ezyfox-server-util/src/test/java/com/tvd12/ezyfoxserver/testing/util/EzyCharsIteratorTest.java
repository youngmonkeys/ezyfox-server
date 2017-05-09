package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyCharsIterator;

public class EzyCharsIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new char[] {'a', 'b', 'c'};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyCharsIterator.wrap(new char[] {'a', 'b', 'c'});
	}

}
