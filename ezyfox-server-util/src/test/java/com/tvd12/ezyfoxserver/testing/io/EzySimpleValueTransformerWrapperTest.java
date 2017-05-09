package com.tvd12.ezyfoxserver.testing.io;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzySimpleValueConverter;
import com.tvd12.ezyfoxserver.io.EzyValueConverter;
import com.tvd12.test.base.BaseTest;

public class EzySimpleValueTransformerWrapperTest extends BaseTest {

	@Test
	public void test() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		assert transformer.convert("true", Boolean.class);
		assert !transformer.convert("false", Boolean.class);
		assert transformer.convert("TRUE", Boolean.class);
		assert !transformer.convert("FALSE", Boolean.class);
		assert transformer.convert(Boolean.TRUE, Boolean.class);
		
		assert transformer.convert("1", Byte.class) == (byte)1;
		assert transformer.convert(1.0D, Byte.class) == (byte)1;
		
		assert transformer.convert(new Character('a'), Character.class) == 'a';
		assert transformer.convert(1.0D, Character.class) == (char)1;
		assert transformer.convert("a", Character.class) == 'a';
		
		assert transformer.convert("1.0", Double.class) == 1.0D;
		assert transformer.convert(1L, Double.class) == 1.0D;

		assert transformer.convert("1.0", Float.class) == 1.0F;
		assert transformer.convert(1L, Float.class) == 1.0F;
		
		assert transformer.convert("1", Integer.class) == 1;
		assert transformer.convert(1.0D, Integer.class) == 1;
		
		assert transformer.convert("1", Long.class) == 1L;
		assert transformer.convert(1.0D, Long.class) == 1L;
		
		assert transformer.convert("1", Short.class) == (short)1;
		assert transformer.convert(1.0D, Short.class) == (short)1;
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test1() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Boolean.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Byte.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test3() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Character.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test31() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert("abc", Character.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test4() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Double.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test5() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Float.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test6() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Integer.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test7() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Long.class);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test8() {
		EzyValueConverter transformer = new EzySimpleValueConverter();
		transformer.convert(new Object(), Short.class);
	}
}
