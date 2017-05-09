package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzyNumbersConverter;
import com.tvd12.test.base.BaseTest;
import static org.testng.Assert.*;
import static com.tvd12.ezyfoxserver.io.EzyNumbersConverter.*;

public class EzyNumbersConverterTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyNumbersConverter.class;
	}
	
	@Test
	public void test() {
		assertEquals(numbersToPrimitiveBytes(Lists.newArrayList(1,2,3)), 
				new byte[] {1, 2, 3});
		assertEquals(numbersToPrimitiveChars(Lists.newArrayList(1,2,3)), 
				new char[] {1, 2, 3});
		assertEquals(numbersToPrimitiveDoubles(Lists.newArrayList(1,2,3)), 
				new double[] {1, 2, 3});
		assertEquals(numbersToPrimitiveFloats(Lists.newArrayList(1,2,3)), 
				new float[] {1, 2, 3});
		assertEquals(numbersToPrimitiveInts(Lists.newArrayList(1,2,3)), 
				new int[] {1, 2, 3});
		assertEquals(numbersToPrimitiveLongs(Lists.newArrayList(1,2,3)), 
				new long[] {1, 2, 3});
		assertEquals(numbersToPrimitiveShorts(Lists.newArrayList(1,2,3)), 
				new short[] {1, 2, 3});
		
		assertEquals(objectsToWrapperNumbers(Lists.newArrayList("1", "2", "3"),
				(size) -> new Long[size], (str) -> Long.valueOf(str)), 
				new Long[] {1L, 2L, 3L});
		
		assertEquals(numbersToWrapperNumbers(Lists.newArrayList(1,2,3),
				(size) -> new Long[size], (number) -> number.longValue()), 
				new Long[] {1L, 2L, 3L});
		
		assertEquals(numbersToWrapperBytes(Lists.newArrayList(1,2,3)), 
				new Byte[] {1, 2, 3});
		assertEquals(numbersToWrapperChars(Lists.newArrayList(1,2,3)), 
				new Character[] {1, 2, 3});
		assertEquals(numbersToWrapperDoubles(Lists.newArrayList(1,2,3)), 
				new Double[] {1D, 2D, 3D});
		assertEquals(numbersToWrapperFloats(Lists.newArrayList(1,2,3)), 
				new Float[] {1F, 2F, 3F});
		assertEquals(numbersToWrapperInts(Lists.newArrayList(1,2,3)), 
				new Integer[] {1, 2, 3});
		assertEquals(numbersToWrapperLongs(Lists.newArrayList(1,2,3)), 
				new Long[] {1L, 2L, 3L});
		assertEquals(numbersToWrapperShorts(Lists.newArrayList(1,2,3)), 
				new Short[] {1, 2, 3});
		assertEquals(objectToChar('a'), new Character('a'));
	}
	
	@Test
	public void convertNumberTest() {
		Byte value = EzyNumbersConverter.convertNumber((Object)new Long(100), n -> n.byteValue());
		System.err.println("value = " + value);
		assert value == 100;
	}
}
