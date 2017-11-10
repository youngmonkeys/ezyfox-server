package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyLiteEntityFactory;
import com.tvd12.ezyfoxserver.io.EzyLiteOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzyOutputTransformer;
import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.ezyfoxserver.testing.entity.EzyEntityTest;
import static org.testng.Assert.*;

public class EzyLiteOutputTransformerTest extends EzyEntityTest {

	private EzyOutputTransformer transformer = new EzyLiteOutputTransformer();
	
	@Test
	public void test() {
		assertEquals(transform(newArray(true, false, true), boolean[].class), 
				new boolean[] {true, false, true});
		assertEquals(transform(newArray((byte)1, (byte)2, (byte)3), byte[].class), 
				new byte[] {(byte)1, (byte)2, (byte)3});
		assertEquals(transform(new byte[] {(byte)1, (byte)2, (byte)3}, byte[].class), 
				new byte[] {(byte)1, (byte)2, (byte)3});
		assertEquals(transform(newArray('a', 'b', 'c'), char[].class), 
				new char[] {'a', 'b', 'c'});
		assertEquals(transform(new byte[] {'a', 'b', 'c'}, char[].class), 
				new char[] {'a', 'b', 'c'});
		assertEquals(transform(newArray(1D, 2D, 3D), double[].class), 
				new double[] {1D, 2D, 3D});
		assertEquals(transform(newArray(1F, 2F, 3F), float[].class), 
				new float[] {1F, 2F, 3F});
		assertEquals(transform(newArray(1, 2, 3), int[].class), 
				new int[] {1, 2, 3});
		assertEquals(transform(newArray(1L, 2L, 3L), long[].class), 
				new long[] {1L, 2L, 3L});
		assertEquals(transform(newArray((short)1, (short)2, (short)3), short[].class), 
				new short[] {(short)1, (short)2, (short)3});
		assertEquals(transform(newArray("a", "b", "c"), String[].class), 
				new String[] {"a", "b", "c"});
		
		assertEquals(transform(newArray(true, false, true), Boolean[].class), 
				new Boolean[] {true, false, true});
		assertEquals(transform(newArray((byte)1, (byte)2, (byte)3), Byte[].class), 
				new Byte[] {(byte)1, (byte)2, (byte)3});
		assertEquals(transform(new byte[] {(byte)1, (byte)2, (byte)3}, Byte[].class), 
				new Byte[] {(byte)1, (byte)2, (byte)3});
		assertEquals(transform(newArray('a', 'b', 'c'), Character[].class), 
				new Character[] {'a', 'b', 'c'});
		assertEquals(transform(new byte[] {'a', 'b', 'c'}, Character[].class), 
				new Character[] {'a', 'b', 'c'});
		assertEquals(transform(newArray(1D, 2D, 3D), Double[].class), 
				new Double[] {1D, 2D, 3D});
		assertEquals(transform(newArray(1F, 2F, 3F), Float[].class), 
				new Float[] {1F, 2F, 3F});
		assertEquals(transform(newArray(1, 2, 3), Integer[].class), 
				new Integer[] {1, 2, 3});
		assertEquals(transform(newArray(1L, 2L, 3L), Long[].class), 
				new Long[] {1L, 2L, 3L});
		assertEquals(transform(newArray((short)1, (short)2, (short)3), Short[].class), 
				new Short[] {(short)1, (short)2, (short)3});
		assertEquals(transform(newArray("a", "b", "c"), String[].class), 
				new String[] {"a", "b", "c"});
	}
	
	@Test
	public void test1() {
		assertEquals(transform(true, boolean.class), true);
		assertEquals(transform(1L, byte.class), (byte)1);
		assertEquals(transform(1L, char.class), (char)1);
		assertEquals(transform(1L, double.class), 1D);
		assertEquals(transform(1L, float.class), (float)1F);
		assertEquals(transform(1L, int.class), (int)1);
		assertEquals(transform((byte)1, long.class), 1L);
		assertEquals(transform(1L, short.class), (short)1);
		
		assertEquals(transform(true, Boolean.class), true);
		assertEquals(transform(1L, Byte.class), (byte)1);
		assertEquals(transform(1L, Character.class), (char)1);
		assertEquals(transform(1L, Double.class), 1D);
		assertEquals(transform(1L, Float.class), (float)1F);
		assertEquals(transform(1L, Integer.class), (int)1);
		assertEquals(transform((byte)1, Long.class), 1L);
		assertEquals(transform(1L, Short.class), (short)1);
		assertEquals(transform("a", String.class), "a");
	}
	
	@Test
	public void test2() {
		String encode = EzyBase64.encode2utf(EzyStrings.getUtfBytes("dungtv"));
		assertEquals(transform(encode, byte[].class), EzyStrings.getUtfBytes("dungtv"));
	}
	
	@Test
	public void test3() {
		char[] chs = new char[] {'d', 'u', 'n', 'g', 't', 'v'};
		String string = new String(chs);
		assertEquals(transform(string, char[].class),chs);
	}
	
	@Test
	public void test4() {
		EzyObject obj = newObjectBuilder()
		.append("1", newArrayBuilder()
				.append(new boolean[] {true, false, true})
				.append(new boolean[] {false, true, false})
				.append(new boolean[] {true, false, true}))
		.append("2", newArrayBuilder()
				.append(new byte[] {1, 2, 3})
				.append(new byte[] {4, 5, 6})
				.append(new byte[] {7, 8, 9}))
		.append("3", newArrayBuilder()
				.append(new char[] {1, 2, 3})
				.append(new char[] {4, 5, 6})
				.append(new char[] {7, 8, 9}))
		.append("4", newArrayBuilder()
				.append(new double[] {1, 2, 3})
				.append(new double[] {4, 5, 6})
				.append(new double[] {7, 8, 9}))
		.append("5", newArrayBuilder()
				.append(new float[] {1, 2, 3})
				.append(new float[] {4, 5, 6})
				.append(new float[] {7, 8, 9}))
		.append("6", newArrayBuilder()
				.append(new int[] {1, 2, 3})
				.append(new int[] {4, 5, 6})
				.append(new int[] {7, 8, 9}))
		.append("7", newArrayBuilder()
				.append(new long[] {1, 2, 3})
				.append(new long[] {4, 5, 6})
				.append(new long[] {7, 8, 9}))
		.append("8", newArrayBuilder()
				.append(new short[] {1, 2, 3})
				.append(new short[] {4, 5, 6})
				.append(new short[] {7, 8, 9}))
		.append("9", newArrayBuilder()
				.append(new String[] {"1", "2", "3"}, 
						new String[] {"4", "5", "6"},
						new String[] {"7", "8", "9"}))
		.append("10", newArrayBuilder()
				.append(new Boolean[] {true, false, true},
						new Boolean[] {false, true, false},
						new Boolean[] {true, false, true}))
		.append("11", newArrayBuilder()
				.append(new Byte[] {1, 2, 3},
						new Byte[] {4, 5, 6},
						new Byte[] {7, 8, 9}))
		.append("12", newArrayBuilder()
				.append(new Character[] {1, 2, 3},
						new Character[] {4, 5, 6},
						new Character[] {7, 8, 9}))
		.append("13", newArrayBuilder()
				.append(new Double[] {1D, 2D, 3D},
						new Double[] {4D, 5D, 6D},
						new Double[] {7D, 8D, 9d}))
		.append("14", newArrayBuilder()
				.append(new Float[] {1F, 2F, 3F},
						new Float[] {4F, 5F, 6F},
						new Float[] {7F, 8F, 9F}))
		.append("15", newArrayBuilder()
				.append(new Integer[] {1, 2, 3},
						new Integer[] {4, 5, 6},
						new Integer[] {7, 8, 9}))
		.append("16", newArrayBuilder()
				.append(new Long[] {1L, 2L, 3L},
						new Long[] {4L, 5L, 6L},
						new Long[] {7L, 8L, 9L}))
		.append("17", newArrayBuilder()
				.append(new Short[] {1, 2, 3},
						new Short[] {4, 5, 6},
						new Short[] {7, 8, 9}))
		.build();
		test(obj);
		
	}
	
	@Test
	public void test5() {
		EzyObject obj = newObjectBuilder()
		.append("1", newArrayBuilder()
				.append(newArrayBuilder().append(true, false, true))
				.append(newArrayBuilder().append(false, true, false))
				.append(newArrayBuilder().append(true, false, true)))
		.append("2", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("3", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("4", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("5", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("6", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("7", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("8", newArrayBuilder()
				.append(newArrayBuilder().append(1, 2, 3))
				.append(newArrayBuilder().append(4, 5, 6))
				.append(newArrayBuilder().append(7, 8, 9)))
		.append("9", newArrayBuilder()
				.append(newArrayBuilder().append("1", "2", "3"))
				.append(newArrayBuilder().append("4", "5", "6"))
				.append(newArrayBuilder().append("7", "8", "9")))
		.append("10", newArrayBuilder()
				.append(newArrayBuilder().append(new Boolean[] {true, false, true}))
				.append(newArrayBuilder().append(new Boolean[] {false, true, false}))
				.append(newArrayBuilder().append(new Boolean[] {true, false, true})))
		.append("11", newArrayBuilder()
				.append(newArrayBuilder().append(new Byte[] {1, 2, 3}))
				.append(newArrayBuilder().append(new Byte[] {4, 5, 6}))
				.append(newArrayBuilder().append(new Byte[] {7, 8, 9})))
		.append("12", newArrayBuilder()
				.append(newArrayBuilder().append(new Character[] {1, 2, 3}))
				.append(newArrayBuilder().append(new Character[] {4, 5, 6}))
				.append(newArrayBuilder().append(new Character[] {7, 8, 9})))
		.append("13", newArrayBuilder()
				.append(newArrayBuilder().append(new Double[] {1D, 2D, 3D}))
				.append(newArrayBuilder().append(new Double[] {4D, 5D, 6D}))
				.append(newArrayBuilder().append(new Double[] {7D, 8D, 9d})))
		.append("14", newArrayBuilder()
				.append(newArrayBuilder().append(new Float[] {1F, 2F, 3F}))
				.append(newArrayBuilder().append(new Float[] {4F, 5F, 6F}))
				.append(newArrayBuilder().append(new Float[] {7F, 8F, 9F})))
		.append("15", newArrayBuilder()
				.append(newArrayBuilder().append(new Integer[] {1, 2, 3}))
				.append(newArrayBuilder().append(new Integer[] {4, 5, 6}))
				.append(newArrayBuilder().append(new Integer[] {7, 8, 9})))
		.append("16", newArrayBuilder()
				.append(newArrayBuilder().append(new Long[] {1L, 2L, 3L}))
				.append(newArrayBuilder().append(new Long[] {4L, 5L, 6L}))
				.append(newArrayBuilder().append(new Long[] {7L, 8L, 9L})))
		.append("17", newArrayBuilder()
				.append(newArrayBuilder().append(new Short[] {1, 2, 3}))
				.append(newArrayBuilder().append(new Short[] {4, 5, 6}))
				.append(newArrayBuilder().append(new Short[] {7, 8, 9})))
		.build();
		test(obj);
		
	}
	
	public void test(EzyObject obj) {
		//check
				assertEquals(obj.get("1", boolean[][].class), new boolean[][] {
						new boolean[] {true, false, true},
						new boolean[] {false, true, false},
						new boolean[] {true, false, true}});
				assertEquals(obj.get("2", byte[][].class), new byte[][] {
						new byte[] {1, 2, 3},
						new byte[] {4, 5, 6},
						new byte[] {7, 8, 9}});
				assertEquals(obj.get("3", char[][].class), new char[][] {
						new char[] {1, 2, 3},
						new char[] {4, 5, 6},
						new char[] {7, 8, 9}});
				assertEquals(obj.get("4", double[][].class), new double[][] {
						new double[] {1, 2, 3},
						new double[] {4, 5, 6},
						new double[] {7, 8, 9}});
				assertEquals(obj.get("5", float[][].class), new float[][] {
						new float[] {1, 2, 3},
						new float[] {4, 5, 6},
						new float[] {7, 8, 9}});
				assertEquals(obj.get("6", int[][].class), new int[][] {
						new int[] {1, 2, 3},
						new int[] {4, 5, 6},
						new int[] {7, 8, 9}});
				assertEquals(obj.get("7", long[][].class), new long[][] {
						new long[] {1, 2, 3},
						new long[] {4, 5, 6},
						new long[] {7, 8, 9}});
				assertEquals(obj.get("8", short[][].class), new short[][] {
						new short[] {1, 2, 3},
						new short[] {4, 5, 6},
						new short[] {7, 8, 9}});
		        assertEquals(obj.get("9", String[][].class), new String[][] {
						new String[] {"1", "2", "3"},
						new String[] {"4", "5", "6"},
						new String[] {"7", "8", "9"}});
				assertEquals(obj.get("10", Boolean[][].class), new Boolean[][] {
						new Boolean[] {true, false, true},
						new Boolean[] {false, true, false},
						new Boolean[] {true, false, true}});
				assertEquals(obj.get("11", Byte[][].class), new Byte[][] {
						new Byte[] {1, 2, 3},
						new Byte[] {4, 5, 6},
						new Byte[] {7, 8, 9}});
				assertEquals(obj.get("12", Character[][].class), new Character[][] {
						new Character[] {1, 2, 3},
						new Character[] {4, 5, 6},
						new Character[] {7, 8, 9}});
				assertEquals(obj.get("13", Double[][].class), new Double[][] {
						new Double[] {1D, 2D, 3D},
						new Double[] {4D, 5D, 6D},
						new Double[] {7D, 8D, 9d}});
				assertEquals(obj.get("14", Float[][].class), new Float[][] {
						new Float[] {1F, 2F, 3F},
						new Float[] {4F, 5F, 6F},
						new Float[] {7F, 8F, 9F}});
				assertEquals(obj.get("15", Integer[][].class), new Integer[][] {
						new Integer[] {1, 2, 3},
						new Integer[] {4, 5, 6},
						new Integer[] {7, 8, 9}});
				assertEquals(obj.get("16", Long[][].class), new Long[][] {
						new Long[] {1L, 2L, 3L},
						new Long[] {4L, 5L, 6L},
						new Long[] {7L, 8L, 9L}});
				assertEquals(obj.get("17", Short[][].class), new Short[][] {
						new Short[] {1, 2, 3},
						new Short[] {4, 5, 6},
						new Short[] {7, 8, 9}});
	}
	
	@SuppressWarnings("rawtypes")
	private Object transform(Object value, Class type) {
		return transformer.transform(value, type);
	}
	
	@SuppressWarnings("unchecked")
	private <T> EzyArray newArray(T... items) {
		return newArrayBuilder().append(items).build();
	}
	
	@Override
	protected EzyObjectBuilder newObjectBuilder() {
		return EzyLiteEntityFactory.create(EzyObjectBuilder.class);
	}
	
	@Override
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyLiteEntityFactory.create(EzyArrayBuilder.class);
	}
	
	public static void main(String[] args) {
		new EzyLiteOutputTransformerTest().test5();
	}
	
	
}
