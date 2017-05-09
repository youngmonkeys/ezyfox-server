package com.tvd12.ezyfoxserver.binding.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleUnmarshaller;
import com.tvd12.test.base.BaseTest;

public class EzySimpleUnmarshallerTest extends BaseTest {

	@Test
	public void test1() {
		EzyUnmarshaller unmarshaller = new EzySimpleUnmarshaller();
		Object object1 = unmarshaller.unmarshal((Object)null, Object.class);
		assertNull(object1);
		
		Integer[] arrayInput = new Integer[] {1, 2, 3};
		Integer[] arrayOutput = unmarshaller.unmarshal(arrayInput, Integer[].class);
		assertEquals(arrayInput, arrayOutput);
		
		Collection<Integer> collectionInput = Lists.newArrayList(1, 2, 3);
		Collection<Integer> collectionOutput = unmarshaller.unmarshalCollection(
				(Object)collectionInput, List.class, Integer.class);
		assertEquals(collectionInput, collectionOutput);
		
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyUnmarshaller unmarshaller = new EzySimpleUnmarshaller();
		unmarshaller.unmarshal(new Object(), Object.class);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		EzyUnmarshaller unmarshaller = new EzySimpleUnmarshaller();
		unmarshaller.unmarshal(ExReader.class, new Object());
	}
	
	@SuppressWarnings("rawtypes")
	public static class ExReader implements EzyReader {

		@Override
		public Object read(EzyUnmarshaller unmarshaller, Object value) {
			return null;
		}
		
	}
	
}
