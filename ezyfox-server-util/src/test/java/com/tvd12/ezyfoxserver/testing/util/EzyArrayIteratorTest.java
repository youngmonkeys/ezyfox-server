package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyArrayIterator;
import static org.testng.Assert.*;

import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EzyArrayIteratorTest {

	@Test
	public void test() {
		Object array = newArray();
		EzyArrayIterator<?> it = newIterator();
		AtomicInteger i = new AtomicInteger(0);
		while(it.hasNext())
			assertEquals(it.next(), Array.get(array, i.getAndIncrement()));
	}

	protected abstract Object newArray();
	protected abstract EzyArrayIterator<?> newIterator();
}
