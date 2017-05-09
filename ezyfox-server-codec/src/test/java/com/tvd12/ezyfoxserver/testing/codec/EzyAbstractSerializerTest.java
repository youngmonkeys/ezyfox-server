package com.tvd12.ezyfoxserver.testing.codec; 
 
import java.util.Map;
 
import org.testng.annotations.Test; 
 
import com.tvd12.ezyfoxserver.codec.EzyAbstractSerializer; 
import com.tvd12.ezyfoxserver.function.EzyParser; 
import com.tvd12.test.base.BaseTest; 
 
public class EzyAbstractSerializerTest extends BaseTest { 
 
	@Test
	public void test() { 
		EzyAbstractSerializer serializer = new Serializer();
		assert serializer.serialize(null) == null;
		assert serializer.serialize(new String("abc")) != null;
	} 
	 
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() { 
		EzyAbstractSerializer serializer = new Serializer();
		serializer.serialize(new EzyAbstractSerializerTest());
	} 
	 
	@Test
	public void test2() { 
		EzyAbstractSerializer serializer = new Serializer1();
		serializer.serialize(new EzyAbstractSerializerTest());
	} 
	 
	public static class Serializer extends EzyAbstractSerializer { 
 
		@Override 
		protected byte[] parseNil() { 
			return null; 
		} 
 
		@Override 
		protected void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers) {
			parsers.put(String.class, new EzyParser<Object, byte[]>() {
				@Override 
				public byte[] parse(Object input) {
					return input.toString().getBytes();
				} 
			}); 
		} 
		 
	} 
	 
	public static class Serializer1 extends EzyAbstractSerializer { 
 
		@Override 
		protected byte[] parseNil() { 
			return null; 
		} 
		 
		@Override 
		protected byte[] parseWithNoParser(Object value) {
			return new byte[] {}; 
		} 
 
		@Override 
		protected void addParsers(Map<Class<?>, EzyParser<Object, byte[]>> parsers) {
			parsers.put(String.class, new EzyParser<Object, byte[]>() {
				@Override 
				public byte[] parse(Object input) {
					return input.toString().getBytes();
				} 
			}); 
		} 
		 
	} 
	 
} 