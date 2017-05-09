package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzySimpleValueConverter;
import com.tvd12.ezyfoxserver.io.EzyValueConverter;
import com.tvd12.test.base.BaseTest;

public class EzySimpleValueTransformerPrimitiveTest extends BaseTest {

	@Test
	public void test() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		assert transformer.convert("true", boolean.class);
		assert !transformer.convert("false", boolean.class);
		assert transformer.convert("TRUE", boolean.class);
		assert !transformer.convert("FALSE", boolean.class);
		assert transformer.convert(Boolean.TRUE, boolean.class);
		
		assert transformer.convert("1", byte.class) == (byte)1;
		assert transformer.convert(1.0D, byte.class) == (byte)1;
		
		assert transformer.convert(new Character('a'), char.class) == 'a';
		assert transformer.convert(1.0D, char.class) == (char)1;
		assert transformer.convert("a", char.class) == 'a';
		
		assert transformer.convert("1.0", double.class) == 1.0D;
		assert transformer.convert(1L, double.class) == 1.0D;

		assert transformer.convert("1.0", float.class) == 1.0F;
		assert transformer.convert(1L, float.class) == 1.0F;
		
		assert transformer.convert("1", int.class) == 1;
		assert transformer.convert(1.0D, int.class) == 1;
		
		assert transformer.convert("1", long.class) == 1L;
		assert transformer.convert(1.0D, long.class) == 1L;
		
		assert transformer.convert("1", short.class) == (short)1;
		assert transformer.convert(1.0D, short.class) == (short)1;
		
		assert transformer.convert(1, String.class).equals("1");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test1() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), boolean.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), byte.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test3() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), char.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test31() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert("abc", char.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test4() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), double.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test5() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), float.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test6() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), int.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test7() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), long.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test8() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), short.class);
	}
}
