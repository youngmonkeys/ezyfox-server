package com.tvd12.ezyfoxserver.testing.io;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzySimpleValueConverter;
import com.tvd12.ezyfoxserver.io.EzyValueConverter;
import com.tvd12.test.base.BaseTest;

public class EzySimpleValueTransformerPrimitiveArraysTest extends BaseTest {

	@Test
	public void test() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		assertEquals(transformer.convert("true,false,true;true,false,true", boolean[][].class), new boolean[][] {{true, false, true},{true, false, true}});
		assertEquals(transformer.convert(new boolean[][] {{true, false, true},{true, false, true}}, boolean[][].class), new boolean[][] {{true, false, true},{true, false, true}});
		assertEquals(transformer.convert(new Boolean[][] {{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE},{Boolean.TRUE, Boolean.FALSE, Boolean.TRUE}}, boolean[][].class), new boolean[][] {{true, false, true},{true, false, true}});
		
		assertEquals(transformer.convert("1,2,3;1,2,3", byte[][].class), new byte[][] {{1, 2, 3}, {1, 2, 3}});
		assertEquals(transformer.convert(new byte[][] {{1, 2, 3}, {1, 2, 3}}, byte[][].class), new byte[][] {{1, 2, 3}, {1, 2, 3}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3}, {1, 2, 3}}, byte[][].class), new byte[][] {{1, 2, 3}, {1, 2, 3}});
		
		assertEquals(transformer.convert("123;123", char[][].class), new char[][] {{'1', '2', '3'},{'1', '2', '3'}});
		assertEquals(transformer.convert(new char[][] {{1, 2, 3},{1, 2, 3}}, char[][].class), new char[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, char[][].class), new char[][] {{1, 2, 3},{1, 2, 3}});
		
		assertEquals(transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", double[][].class), new double[][] {{1.1D, 2.2D, 3.3D},{1.1D, 2.2D, 3.3D}});
		assertEquals(transformer.convert(new double[][] {{1D, 2D, 3D},{1D, 2D, 3D}}, double[][].class), new double[][] {{1.0D, 2.0D, 3.0D}, {1.0D, 2.0D, 3.0D}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, double[][].class), new double[][] {{1.0D, 2.0D, 3.0D},{1.0D, 2.0D, 3.0D}});
		
		assertEquals(transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", float[][].class), new float[][] {{1.1F, 2.2F, 3.3F},{1.1F, 2.2F, 3.3F}});
		assertEquals(transformer.convert(new float[][] {{1F,2F,3F},{1F, 2F, 3F}}, float[][].class), new float[][] {{1.0F, 2.0F, 3.0F}, {1.0F, 2.0F, 3.0F}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, float[][].class), new float[][] {{1.0F, 2.0F, 3.0F},{1.0F, 2.0F, 3.0F}});
		
		assertEquals(transformer.convert("1,2,3;1,2,3", int[][].class), new int[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new int[][] {{1, 2, 3},{1, 2, 3}}, int[][].class), new int[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, int[][].class), new int[][] {{1, 2, 3},{1, 2, 3}});
		
		assertEquals(transformer.convert("1,2,3;1,2,3", long[][].class), new long[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new long[][] {{1L,2L,3L},{1L, 2L, 3L}}, long[][].class), new long[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, long[][].class), new long[][] {{1, 2, 3},{1, 2, 3}});
		
		assertEquals(transformer.convert("1,2,3;1,2,3", short[][].class), new short[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new short[][] {{1, 2, 3},{1, 2, 3}}, short[][].class), new short[][] {{1, 2, 3},{1, 2, 3}});
		assertEquals(transformer.convert(new Integer[][] {{1, 2, 3},{1, 2, 3}}, short[][].class), new short[][] {{1, 2, 3},{1, 2, 3}});
		
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test1() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), boolean[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), byte[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test3() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), char[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test4() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), double[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test5() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), float[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test6() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), int[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test7() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), long[][].class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test8() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), short[][].class);
	}
}
