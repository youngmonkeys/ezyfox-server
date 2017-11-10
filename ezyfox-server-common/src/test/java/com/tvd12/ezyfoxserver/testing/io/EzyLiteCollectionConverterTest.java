package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyLiteEntityFactory;
import com.tvd12.ezyfoxserver.io.EzyCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyLiteCollectionConverter;
import com.tvd12.ezyfoxserver.io.EzyLiteOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzySimpleCollectionConverter;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.testing.CommonBaseTest;

public class EzyLiteCollectionConverterTest extends CommonBaseTest {

	private EzyCollectionConverter collectionConverter;
	
	public EzyLiteCollectionConverterTest() {
		super();
		this.collectionConverter = new EzyLiteCollectionConverter(new EzyLiteOutputTransformer());
	}
	
//	@Test
	public void test() {
		Collection<Boolean> booleans = Lists.newArrayList(true, false, true);
		assertEquals(collectionConverter.toArray(booleans, Boolean.class), 
				new Boolean[] {true, false, true});
		
		Collection<Byte> bytes = Lists.newArrayList((byte)1, (byte)2, (byte)3);
		assertEquals(collectionConverter.toArray(bytes, Byte.class), 
				new Byte[] {(byte)1, (byte)2, (byte)3});
		
		Collection<Character> characters = Lists.newArrayList('a', 'b', 'c');
		assertEquals(collectionConverter.toArray(characters, Character.class), 
				new Character[] {'a', 'b', 'c'});
		
		Collection<Double> doubles = Lists.newArrayList(1D, 2D, 3D);
		assertEquals(collectionConverter.toArray(doubles, Double.class), 
				new Double[] {1D, 2D, 3D});
		
		Collection<Float> floats = Lists.newArrayList(1F, 2F, 3F);
		assertEquals(collectionConverter.toArray(floats, Float.class), 
				new Float[] {1F, 2F, 3F});
		
		Collection<Integer> integers = Lists.newArrayList(1, 2, 3);
		assertEquals(collectionConverter.toArray(integers, Integer.class), 
				new Integer[] {1, 2, 3});
		
		Collection<Long> longs = Lists.newArrayList(1L, 2L, 3L);
		assertEquals(collectionConverter.toArray(longs, Long.class), 
				new Long[] {1L, 2L, 3L});
		
		Collection<Short> shorts = Lists.newArrayList((short)1, (short)2, (short)3);
		assertEquals(collectionConverter.toArray(shorts, Short.class), 
				new Short[] {(short)1, (short)2, (short)3});
		
		Collection<String> strings = Lists.newArrayList("a", "b", "c");
		assertEquals(collectionConverter.toArray(strings, String.class), 
				new String[] {"a", "b", "c"});
	}
	
//	@Test
	public void test1() {
		Collection<Boolean> booleans = Lists.newArrayList(true, false, true);
		assertEquals(collectionConverter.toArray(booleans, boolean.class), 
				new boolean[] {true, false, true});
		
		Collection<Byte> bytes = Lists.newArrayList((byte)1, (byte)2, (byte)3);
		assertEquals(collectionConverter.toArray(bytes, byte.class), 
				new byte[] {(byte)1, (byte)2, (byte)3});
		
		Collection<Character> characters = Lists.newArrayList('a', 'b', 'c');
		assertEquals(collectionConverter.toArray(characters, char.class), 
				new char[] {'a', 'b', 'c'});
		
		Collection<Double> doubles = Lists.newArrayList(1D, 2D, 3D);
		assertEquals(collectionConverter.toArray(doubles, double.class), 
				new double[] {1D, 2D, 3D});
		
		Collection<Float> floats = Lists.newArrayList(1F, 2F, 3F);
		assertEquals(collectionConverter.toArray(floats, float.class), 
				new float[] {1F, 2F, 3F});
		
		Collection<Integer> integers = Lists.newArrayList(1, 2, 3);
		assertEquals(collectionConverter.toArray(integers, int.class), 
				new int[] {1, 2, 3});
		
		Collection<Long> longs = Lists.newArrayList(1L, 2L, 3L);
		assertEquals(collectionConverter.toArray(longs, long.class), 
				new long[] {1L, 2L, 3L});
		
		Collection<Short> shorts = Lists.newArrayList((short)1, (short)2, (short)3);
		assertEquals(collectionConverter.toArray(shorts, short.class), 
				new short[] {(short)1, (short)2, (short)3});
		
		Collection<String> strings = Lists.newArrayList("a", "b", "c");
		assertEquals(collectionConverter.toArray(strings, String.class), 
				new String[] {"a", "b", "c"});
	}
	
	@Test
	public void test2() {
		Collection<EzyArray> booleanss = new ArrayList<>();
		booleanss.add(newArrayBuilder().append(true, false, true).build());
		booleanss.add(newArrayBuilder().append(false, true, false).build());
		assertEquals(collectionConverter.toArray(booleanss, boolean[].class), 
				new boolean[][] {{true, false, true}, {false, true, false}});
		
		Collection<EzyArray> bytess = new ArrayList<>();
		bytess.add(newArrayBuilder().append((byte)1, (byte)2, (byte)3).build());
		bytess.add(newArrayBuilder().append((byte)4, (byte)5, (byte)6).build());
		assertEquals(collectionConverter.toArray(bytess, byte[].class), 
				new byte[][] {{(byte)1, (byte)2, (byte)3}, {(byte)4, (byte)5, (byte)6}});
		
		Collection<EzyArray> characterss = new ArrayList<>();
		characterss.add(newArrayBuilder().append('a', 'b', 'c').build());
		characterss.add(newArrayBuilder().append('d', 'e', 'f').build());
		assertEquals(collectionConverter.toArray(characterss, char[].class), 
				new char[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
		
		Collection<EzyArray> doubless = new ArrayList<>();
		doubless.add(newArrayBuilder().append(1D, 2D, 3D).build());
		doubless.add(newArrayBuilder().append(4D, 5D, 6D).build());
		assertEquals(collectionConverter.toArray(doubless, double[].class), 
				new double[][] {{1D, 2D, 3D}, {4D, 5D, 6D}});
		
		Collection<EzyArray> floatss = new ArrayList<>();
		floatss.add(newArrayBuilder().append(1F, 2F, 3F).build());
		floatss.add(newArrayBuilder().append(4F, 5F, 6F).build());
		assertEquals(collectionConverter.toArray(floatss, float[].class), 
				new float[][] {{1F, 2F, 3F}, {4F, 5F, 6F}});
		
		Collection<EzyArray> integerss = new ArrayList<>();
		integerss.add(newArrayBuilder().append(1, 2, 3).build());
		integerss.add(newArrayBuilder().append(4, 5, 6).build());
		assertEquals(collectionConverter.toArray(integerss, int[].class), 
				new int[][] {{1, 2, 3}, {4, 5, 6}});
		
		Collection<EzyArray> longss = new ArrayList<>();
		longss.add(newArrayBuilder().append(1L, 2L, 3L).build());
		longss.add(newArrayBuilder().append(4L, 5L, 6L).build());
		assertEquals(collectionConverter.toArray(longss, long[].class), 
				new long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
		
		Collection<EzyArray> shortss = new ArrayList<>();
		shortss.add(newArrayBuilder().append((short)1, (short)2, (short)3).build());
		shortss.add(newArrayBuilder().append((short)4, (short)5, (short)6).build());
		assertEquals(collectionConverter.toArray(shortss, short[].class), 
				new short[][] {{(short)1, (short)2, (short)3}, {(short)4, (short)5, (short)6}});
		
		Collection<EzyArray> stringss = new ArrayList<>();
		stringss.add(newArrayBuilder().append("a", "b", "c").build());
		stringss.add(newArrayBuilder().append("d", "e", "f").build());
		assertEquals(collectionConverter.toArray(stringss, String[].class), 
				new String[][] {{"a", "b", "c"}, {"d", "e", "f"}});
	}
	
	
	@Test
	public void test3() {
		Collection<EzyArray> booleanss = new ArrayList<>();
		booleanss.add(newArrayBuilder().append(true, false, true).build());
		booleanss.add(newArrayBuilder().append(false, true, false).build());
		assertEquals(collectionConverter.toArray(booleanss, Boolean[].class), 
				new Boolean[][] {{true, false, true}, {false, true, false}});
		
		Collection<EzyArray> bytess = new ArrayList<>();
		bytess.add(newArrayBuilder().append((byte)1, (byte)2, (byte)3).build());
		bytess.add(newArrayBuilder().append((byte)4, (byte)5, (byte)6).build());
		assertEquals(collectionConverter.toArray(bytess, Byte[].class), 
				new Byte[][] {{(byte)1, (byte)2, (byte)3}, {(byte)4, (byte)5, (byte)6}});
		
		Collection<EzyArray> characterss = new ArrayList<>();
		characterss.add(newArrayBuilder().append('a', 'b', 'c').build());
		characterss.add(newArrayBuilder().append('d', 'e', 'f').build());
		assertEquals(collectionConverter.toArray(characterss, Character[].class), 
				new Character[][] {{'a', 'b', 'c'}, {'d', 'e', 'f'}});
		
		Collection<EzyArray> doubless = new ArrayList<>();
		doubless.add(newArrayBuilder().append(1D, 2D, 3D).build());
		doubless.add(newArrayBuilder().append(4D, 5D, 6D).build());
		assertEquals(collectionConverter.toArray(doubless, Double[].class), 
				new Double[][] {{1D, 2D, 3D}, {4D, 5D, 6D}});
		
		Collection<EzyArray> floatss = new ArrayList<>();
		floatss.add(newArrayBuilder().append(1F, 2F, 3F).build());
		floatss.add(newArrayBuilder().append(4F, 5F, 6F).build());
		assertEquals(collectionConverter.toArray(floatss, Float[].class), 
				new Float[][] {{1F, 2F, 3F}, {4F, 5F, 6F}});
		
		Collection<EzyArray> integerss = new ArrayList<>();
		integerss.add(newArrayBuilder().append(1, 2, 3).build());
		integerss.add(newArrayBuilder().append(4, 5, 6).build());
		assertEquals(collectionConverter.toArray(integerss, Integer[].class), 
				new Integer[][] {{1, 2, 3}, {4, 5, 6}});
		
		Collection<EzyArray> longss = new ArrayList<>();
		longss.add(newArrayBuilder().append(1L, 2L, 3L).build());
		longss.add(newArrayBuilder().append(4L, 5L, 6L).build());
		assertEquals(collectionConverter.toArray(longss, Long[].class), 
				new Long[][] {{1L, 2L, 3L}, {4L, 5L, 6L}});
		
		Collection<EzyArray> shortss = new ArrayList<>();
		shortss.add(newArrayBuilder().append((short)1, (short)2, (short)3).build());
		shortss.add(newArrayBuilder().append((short)4, (short)5, (short)6).build());
		assertEquals(collectionConverter.toArray(shortss, Short[].class), 
				new Short[][] {{(short)1, (short)2, (short)3}, {(short)4, (short)5, (short)6}});
		
		Collection<EzyArray> stringss = new ArrayList<>();
		stringss.add(newArrayBuilder().append("a", "b", "c").build());
		stringss.add(newArrayBuilder().append("d", "e", "f").build());
		assertEquals(collectionConverter.toArray(stringss, String[].class), 
				new String[][] {{"a", "b", "c"}, {"d", "e", "f"}});
	}
	
	@Test
	public void test5() {
		try {
			Method method = EzyLiteCollectionConverter.class
					.getDeclaredMethod("toArray", Object.class, Class.class);
			method.setAccessible(true);
			method.invoke(collectionConverter, new String("abc"), String.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test6() {
		new EzySimpleCollectionConverter().toArray(Lists.newArrayList(), Void.class);
	}
	
	@Test
	private void test7() {
		newArrayBuilder()
			.append(new byte[] {1,2,3}, new byte[] {4,5,6}, new byte[]{7,8,9})
			.build();
		EzyArray array = EzyLiteEntityFactory.create(EzyArrayBuilder.class)
			.append(EzyBase64.encode2utf(new byte[] {1,2,3}))
			.append(EzyBase64.encode2utf(new byte[] {4,5,6}))
			.append(EzyBase64.encode2utf(new byte[] {7,8,9}))
			.build();
		assertEquals(array.toArray(byte[].class), new byte[][] {
			{1, 2, 3}, {4, 5, 6}, {7, 8, 9}
		});
	}
	
}
