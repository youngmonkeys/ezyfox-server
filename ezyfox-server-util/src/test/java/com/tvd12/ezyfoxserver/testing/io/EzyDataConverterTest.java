package com.tvd12.ezyfoxserver.testing.io;

import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveBoolArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveIntArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveLongArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToPrimitiveShortArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToStringArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperDoubleArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperFloatArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperIntArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperLongArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.collectionToWrapperShortArray;
import static com.tvd12.ezyfoxserver.io.EzyDataConverter.toCharWrapperArray;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.io.EzyDataConverter;
import com.tvd12.test.base.BaseTest; 

public class EzyDataConverterTest extends BaseTest {

	@Test
	public void stringToCharTest() {
		char ch0 = EzyDataConverter.stringToChar("");
		assert ch0 == 0;
		char ch1 = EzyDataConverter.stringToChar(null);
		assert ch1 == 0;
		char ch2 = EzyDataConverter.stringToChar("1,23");
		assert ch2 == '1';
	}
	
	@Test
	public void byteArrayToCharArrayTest() {
		char[] chs = EzyDataConverter.byteArrayToCharArray(new byte[] {1, 2, 3});
		assertEquals(chs, new char[] {1,2,3});
	}
	
	@Test
	public void charArrayToByteArrayTest() {
		byte[] bytes = EzyDataConverter.charArrayToByteArray(new char[] {1, 2, 3});
		assertEquals(bytes, new byte[] {1, 2, 3});
	}
	
	@Test
	public void collectionToPrimitiveByteArrayTest() {
		byte[] value = EzyDataConverter.collectionToPrimitiveByteArray(
				Lists.newArrayList((byte)1, (byte)2, (byte)3));
		assertEquals(value, new byte[] {1, 2, 3});
	}
	
	@Test
	public void collectionToPrimitiveCharArrayTest() {
		char[] value = EzyDataConverter.collectionToPrimitiveCharArray(
				Lists.newArrayList('a', 'b', 'c'));
		assertEquals(value, new char[] {'a', 'b', 'c'});
	}
	
	@Test
	public void charCollectionToPrimitiveByteArrayTest() {
		byte[] value = EzyDataConverter.charCollectionToPrimitiveByteArray(
				Lists.newArrayList('a', 'b', 'c'));
		assertEquals(value, new byte[] {'a', 'b', 'c'});
	}
	
	@Test
	public void arrayToListTest() {
		List<Byte> bytes = EzyDataConverter.arrayToList(new byte[] {1, 2, 3});
		assertEquals(bytes.containsAll(Lists.newArrayList((byte)1, (byte)2, (byte)3)), true);
	}
	
	@Test
	public void arrayToListTest2() {
		List<String> bytes = EzyDataConverter.arrayToList(new String[] {"a", "b", "c"});
		assertEquals(bytes.containsAll(Lists.newArrayList("a", "b", "c")), true);
	}
	
	@Test
	public void arrayToListBooleanTest() {
		List<Boolean> booleans = EzyDataConverter.arrayToList(new boolean[] {true, false, true});
		assertEquals(booleans.containsAll(Lists.newArrayList(true, false, true)), true);
	}
	
	@Test
	public void arrayToListCharTest() {
		List<Character> booleans = EzyDataConverter.arrayToList(new char[] {'a', 'b', 'c'});
		assertEquals(booleans.containsAll(Lists.newArrayList('a', 'b', 'c')), true);
	}
	
	@Test
	public void arrayToListDoubleTest() {
		List<Double> booleans = EzyDataConverter.arrayToList(new double[] {1D, 2D, 3D});
		assertEquals(booleans.containsAll(Lists.newArrayList(1D, 2D, 3D)), true);
	}
	
	@Test
	public void arrayToListFloatTest() {
		List<Float> booleans = EzyDataConverter.arrayToList(new float[] {1F, 2F, 3F});
		assertEquals(booleans.containsAll(Lists.newArrayList(1F, 2F, 3F)), true);
	}
	
	@Test
	public void arrayToListIntTest() {
		List<Integer> ints = EzyDataConverter.arrayToList(new int[] {1, 2, 3});
		assertEquals(ints.containsAll(Lists.newArrayList(1, 2, 3)), true);
	}
	
	@Test
	public void arrayToListLongTest() {
		List<Long> booleans = EzyDataConverter.arrayToList(new long[] {1L, 2L, 3L});
		assertEquals(booleans.containsAll(Lists.newArrayList(1L, 2L, 3L)), true);
	}
	
	@Test
	public void arrayToListShortTest() {
		List<Short> booleans = EzyDataConverter.arrayToList(new short[] {(short)1, (short)2, (short)3});
		assertEquals(booleans.containsAll(Lists.newArrayList((short)1, (short)2, (short)3)), true);
	}
	
	@Test
	public void byteArrayToCharListTest() {
		List<Character> chars = EzyDataConverter.byteArrayToCharList(new byte[] {'a', 'b', 'c'});
		assertEquals(chars.containsAll(Lists.newArrayList('a', 'b', 'c')), true);
	}
	
	@Test
	public void toBoolWrapperArrayTest() {
		assertEquals(EzyDataConverter.toBoolWrapperArray(
				new boolean[] {true, false, true}), 
				new Boolean[] {true, false, true});
	}
	
	@Test
	public void toByteWrapperArrayTest() {
		assertEquals(EzyDataConverter.toByteWrapperArray(
				new byte[] {1, 2, 3}), 
				new Byte[] {1, 2, 3});
	}
	
	@Test
	public void toPrimitiveByteArrayTest() {
		assertEquals(EzyDataConverter.toPrimitiveByteArray(
				new Byte[] {1, 2, 3}), 
				new byte[] {1, 2, 3});
	}
	
	@Test
	public void toPrimitiveCharArrayTest() {
		assertEquals(EzyDataConverter.toPrimitiveCharArray(
				new Character[] {'a', 'b', 'c'}), 
				new char[] {'a', 'b', 'c'});
	}
	
	@Test
	public void charWrapperArrayToPrimitiveByteArrayTest() {
		assertEquals(EzyDataConverter.charWrapperArrayToPrimitiveByteArray(
				new Character[] {'a', 'b', 'c'}), 
				new byte[] {'a', 'b', 'c'});
	}
	
	@Test
	public void primitiveArrayToBoolCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToBoolCollection(
				new boolean[] {true, false, true}), 
				Lists.newArrayList(true, false, true));
	}
	
	@Test
	public void primitiveArrayToByteCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToByteCollection(
				new byte[] {1, 2, 3}), 
				Lists.newArrayList((byte)1, (byte)2, (byte)3));
	}
	
	@Test
	public void primitiveArrayToCharCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToCharCollection(
				new char[] {'a', 'b', 'c'}), 
				Lists.newArrayList('a', 'b', 'c'));
	}
	
	@Test
	public void primitiveArrayToDoubleCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToDoubleCollection(
				new double[] {1D, 2D, 3D}), 
				Lists.newArrayList(1D, 2D, 3D));
	}
	
	@Test
	public void primitiveArrayToFloatCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToFloatCollection(
				new float[] {1F, 2F, 3F}), 
				Lists.newArrayList(1F, 2F, 3F));
	}
	
	@Test
	public void primitiveArrayToIntCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToIntCollection(
				new int[] {1, 2, 3}), 
				Lists.newArrayList(1, 2, 3));
	}
	
	@Test
	public void primitiveArrayToLongCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToLongCollection(
				new long[] {1, 2, 3}), 
				Lists.newArrayList(1L, 2L, 3L));
	}
	
	@Test
	public void primitiveArrayToShortCollectionTest() {
		assertEquals(EzyDataConverter.primitiveArrayToShortCollection(
				new short[] {1, 2, 3}), 
				Lists.newArrayList((short)1, (short)2, (short)3));
	}
	
	@Test
	public void stringArrayToCollectionTest() {
		assertEquals(EzyDataConverter.stringArrayToCollection(
				new String[] {"a", "b", "c"}), 
				Lists.newArrayList("a", "b", "c"));
	}
	
	@Test
	public void wrapperArrayToCollectionTest() {
		assertEquals(EzyDataConverter.wrapperArrayToCollection(
				new String[] {"a", "b", "c"}), 
				Lists.newArrayList("a", "b", "c"));
	}
	
	@Test
	public void wrapperArrayToCollection2Test() {
		assertEquals(EzyDataConverter.wrapperArrayToCollection(
				(Object) new String[] {"a", "b", "c"}), 
				Lists.newArrayList("a", "b", "c"));
	}
	
	@Test
	public void collectionToWrapperBoolArrayTest() {
		Boolean[] bools = EzyDataConverter.collectionToWrapperBoolArray(
				Lists.newArrayList(true, false, true));
		assertEquals(bools, new Boolean[] {true, false, true});
	}
	
	@Test
	public void collectionToWrapperByteArrayTest() {
		Byte[] bytes = EzyDataConverter.collectionToWrapperByteArray(
				Lists.newArrayList(new Byte[] {1, 2, 3}));
		assertEquals(bytes, new Byte[] {1, 2, 3});
	}
	
	@Test
	public void collectionToWrapperCharArrayTest() {
		Character[] chars = EzyDataConverter.collectionToWrapperCharArray(
				Lists.newArrayList(new Character[] {1, 2, 3}));
		assertEquals(chars, new Character[] {1, 2, 3});
	}
	
	@Test
	public void testAll() {
		assertEquals(collectionToWrapperDoubleArray(Lists.newArrayList(1D, 2D, 3D)), 
				new Double[] {1D, 2D, 3D});
		assertEquals(collectionToWrapperFloatArray(Lists.newArrayList(1F, 2F, 3F)), 
				new Float[] {1F, 2F, 3F});
		assertEquals(collectionToWrapperIntArray(Lists.newArrayList(1, 2, 3)), 
				new Integer[] {1, 2, 3});
		assertEquals(collectionToWrapperLongArray(Lists.newArrayList(1L, 2L, 3L)), 
				new Long[] {1L, 2L, 3L});
		assertEquals(collectionToWrapperShortArray(Lists.newArrayList((short)1, (short)2, (short)3)), 
				new Short[] {1, 2, 3});
		
		assertEquals(collectionToPrimitiveBoolArray(Lists.newArrayList(true, false, true)), 
				new boolean[] {true, false, true});
		assertEquals(collectionToPrimitiveDoubleArray(Lists.newArrayList(1D, 2D, 3D)), 
				new double[] {1D, 2D, 3D});
		assertEquals(collectionToPrimitiveFloatArray(Lists.newArrayList(1F, 2F, 3F)), 
				new float[] {1F, 2F, 3F});
		assertEquals(collectionToPrimitiveIntArray(Lists.newArrayList(1, 2, 3)), 
				new int[] {1, 2, 3});
		assertEquals(collectionToPrimitiveLongArray(Lists.newArrayList(1L, 2L, 3L)), 
				new long[] {1L, 2L, 3L});
		assertEquals(collectionToPrimitiveShortArray(Lists.newArrayList((short)1, (short)2, (short)3)), 
				new short[] {(short)1, (short)2, (short)3});
		assertEquals(collectionToStringArray(Lists.newArrayList("1", "2", "3")), 
				new String[] {"1", "2", "3"});
		
		assertEquals(toCharWrapperArray(new byte[] {'a', 'b', 'c'}), 
				new Character[] {'a', 'b', 'c'});
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyDataConverter.class;
	}
	
}
