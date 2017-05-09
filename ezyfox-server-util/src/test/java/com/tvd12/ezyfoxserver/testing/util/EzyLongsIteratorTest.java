package com.tvd12.ezyfoxserver.testing.util;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import com.tvd12.ezyfoxserver.util.EzyLongsIterator;

public class EzyLongsIteratorTest extends EzyArrayIteratorTest {

	@Override
	protected Object newArray() {
		return new long[] {1, 2, 3};
	}

	@Override
	protected EzyArrayIterator<?> newIterator() {
		return EzyLongsIterator.wrap(new long[] {1, 2, 3});
	}

}
