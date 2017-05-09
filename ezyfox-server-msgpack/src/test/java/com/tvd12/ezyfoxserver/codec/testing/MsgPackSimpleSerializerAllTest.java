package com.tvd12.ezyfoxserver.codec.testing;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.codec.MsgPackConstant;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;

import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MsgPackSimpleSerializerAllTest extends CodecBaseTest {

	private MsgPackSimpleSerializer serializer
			= new MsgPackSimpleSerializer();
	private MsgPackSimpleDeserializer deserializer
			= new MsgPackSimpleDeserializer();
	
	@Test
	public void test() {
		Boolean[] value = new Boolean[] {true, false, true};
		byte[] bytes = serializer.serialize(value);
		assertEquals(bytes, new byte[] {(byte) (0x90 | 3), (byte) 0xc3, (byte) 0xc2, (byte) 0xc3});
		EzyArray array = deserializer.deserialize(bytes);
		assertEquals(value, array.toArray(Boolean.class));
	}
	
	@Test
	public void test1() {
		byte[] bytes = serializer.serialize(new boolean[] {true, false, true});
		EzyArray array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(boolean.class), new boolean[] {true, false, true});
		
		bytes = serializer.serialize(new char[] {'1', 'b', 'c'});
		byte[] bytes1 = deserializer.deserialize(bytes);
		assertEquals(bytes1, new byte[] {'1', 'b', 'c'});
		
		bytes = serializer.serialize(new double[] {1D, 2D, 3D});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(double.class), new double[] {1D, 2D, 3D});
		
		bytes = serializer.serialize(new float[] {1F, 2F, 3F});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(float.class), new float[] {1F, 2F, 3F});
		
		bytes = serializer.serialize(new int[] {1, 2, 3});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(int.class), new int[] {1, 2, 3});
		
		bytes = serializer.serialize(new long[] {1L, 2L, 3L});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(long.class), new long[] {1L, 2L, 3L});
		
		bytes = serializer.serialize(new short[] {(short)1, (short)2, (short)3});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(short.class), new short[] {(short)1, (short)2, (short)3});
		
		bytes = serializer.serialize(new String[] {"1", "2", "3"});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(String.class), new String[] {"1", "2", "3"});
	}
	
	@Test
	public void test2() {
		byte[] bytes = serializer.serialize(new Boolean[] {true, false, true});
		EzyArray array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(Boolean.class), new Boolean[] {true, false, true});
		
		bytes = serializer.serialize(new Byte[] {'1', 'b', 'c'});
		byte[] bytes1 = deserializer.deserialize(bytes);
		assertEquals(bytes1, new byte[] {'1', 'b', 'c'});
		
		bytes = serializer.serialize(new Character[] {'1', 'b', 'c'});
		byte[] bytes2 = deserializer.deserialize(bytes);
		assertEquals(bytes2, new byte[] {'1', 'b', 'c'});
		
		bytes = serializer.serialize(new Double[] {1D, 2D, 3D});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(double.class), new double[] {1D, 2D, 3D});
		
		bytes = serializer.serialize(new Float[] {1F, 2F, 3F});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(float.class), new float[] {1F, 2F, 3F});
		
		bytes = serializer.serialize(new Integer[] {1, 2, 3});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(int.class), new int[] {1, 2, 3});
		
		bytes = serializer.serialize(new Long[] {1L, 2L, 3L});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(long.class), new long[] {1L, 2L, 3L});
		
		bytes = serializer.serialize(new Short[] {(short)1, (short)2, (short)3});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(short.class), new short[] {(short)1, (short)2, (short)3});
		
		bytes = serializer.serialize(new String[] {"1", "2", "3"});
		array = deserializer.deserialize(bytes);
		assertEquals(array.toArray(String.class), new String[] {"1", "2", "3"});
	}
	
	@Test
	public void test3() {
		byte[] bytes = serializer.serialize((byte)12);
		Number number = deserializer.deserialize(bytes);
		assertEquals((Byte)number.byteValue(), new Byte((byte)12));
		
		bytes = serializer.serialize('a');
		number = deserializer.deserialize(bytes);
		assertEquals((char)number.byteValue(), 'a');
		
		bytes = serializer.serialize(new Short((short)10));
		number = deserializer.deserialize(bytes);
		assertEquals(number.shortValue(), (short)10);
		
		Map<String, Long> map = new HashMap<>();
		map.put("a", 1L);
		map.put("b", 2L);
		bytes = serializer.serialize(map);
		EzyObject obj = deserializer.deserialize(bytes);
		assertEquals(obj.size(), 2);
		assertEquals(obj.get("a", Long.class), new Long(1L));
		assertEquals(obj.get("b", Long.class), new Long(2L));
		
		Collection<String> coll = Lists.newArrayList("a", "b", "c");
		bytes = serializer.serialize(coll);
		EzyArray arr = deserializer.deserialize(bytes);
		assertEquals(arr.toList(String.class), Lists.newArrayList("a", "b", "c"));
	}
	
	@Test
	public void test4() {
		byte[] bytes = new byte[MsgPackConstant.MAX_BIN16_SIZE];
		bytes[123] = 5;
		byte[] bs1 = serializer.serialize(bytes);
		byte[] bs2 = deserializer.deserialize(bs1);
		assertEquals(bs2, bytes);
	}
	
	@Test
	public void test5() {
		int[] ints = new int[MsgPackConstant.MAX_ARRAY16_SIZE + 1];
		ints[12] = 5;
		byte[] bs1 = serializer.serialize(ints);
		EzyArray array = deserializer.deserialize(bs1);
		assertEquals(array.size(), ints.length);
		assertEquals(array.get(12, int.class), new Integer(5));
		assertTrue(Arrays.equals(array.toArray(int.class), ints));
		assertEquals(array.toArray(int.class), ints);
	}
	
	@Test
	public void test6() {
		byte[] bytes = serializer.serialize(0);
		assertEquals(bytes, new byte[] {0});
	}
	
	@Test
	public void test7() {
		Map<Object, Object> map = new HashMap<>();
		for(int i = 0 ; i < MsgPackConstant.MAX_MAP16_SIZE + 1 ; i++)
			map.put(String.valueOf(i), i);
		byte[] bytes = serializer.serialize(map);
		EzyObject object = deserializer.deserialize(bytes);
		assertEquals(object.size(), map.size());
		for(Object key : map.keySet())
			assertEquals(object.get(key), map.get(key));
	}
	
	@Test
	public void test8() {
		Map<Object, Object> map = new HashMap<>();
		for(int i = 0 ; i < MsgPackConstant.MAX_MAP16_SIZE ; i++)
			map.put(String.valueOf(i), i);
		byte[] bytes = serializer.serialize(map);
		EzyObject object = deserializer.deserialize(bytes);
		assertEquals(object.size(), map.size());
		for(Object key : map.keySet())
			assertEquals(object.get(key), map.get(key));
	}
	
	@Test
	public void test9() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < MsgPackConstant.MAX_STR8_SIZE + 1 ; i++)
			builder.append(i);
		String str = builder.toString();
		long startTime = System.currentTimeMillis();
		byte[] bs1 = serializer.serialize(str);
		String str2 = deserializer.deserialize(bs1);
		long endTime = System.currentTimeMillis();
		System.err.println("time = " + (endTime - startTime));
		assertEquals(str2, str);
	}
	
	public static void main(String[] args) {
		new MsgPackSimpleSerializerAllTest().test9();
	}
}
