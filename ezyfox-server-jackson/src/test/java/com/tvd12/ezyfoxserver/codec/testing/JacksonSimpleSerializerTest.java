package com.tvd12.ezyfoxserver.codec.testing;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleDeserializer;
import com.tvd12.ezyfoxserver.codec.JacksonSimpleSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.sercurity.EzyBase64;
import com.tvd12.test.base.BaseTest;

import lombok.Data;

public class JacksonSimpleSerializerTest extends BaseTest {

	private ObjectMapper objectMapper 
				= new ObjectMapper();
	private EzyMessageSerializer serializer 
				= new JacksonSimpleSerializer(objectMapper);
	private EzyMessageDeserializer deserializer
				= new JacksonSimpleDeserializer(objectMapper);
	
	@Test
	public void test() throws JsonProcessingException {
		assertEquals(serializer.serialize(null), objectMapper.writeValueAsBytes(null));
		byte[] bytes = serializer.serialize(new ClassA());
		EzyObject object = deserializer.deserialize(bytes);
		assert object.size() == 14;
		assert object.get("name").equals("abc");
		assert object.get("value", Long.class) == 123;
		assertEquals(object.get("parents", String[].class), 
				new String[] {"dungtv", "tuyennc", "hunglq"});
		assert object.get("yesNo", boolean.class);
		System.out.println(Arrays.toString(object.get("bytes", byte[].class)));
		assertEquals(object.get("bytes", byte[].class), new byte[] {1, 2, 3, 4, 5});
		assertEquals(object.get("longValue", Long.class), null);
		assertEquals(object.get("chs", char[].class), new char[] {'1', '2', '3'});
		assertEquals(object.get("doubleValue", Double.class), 123.456D);
		assertEquals(object.get("classC", EzyObject.class).get("name"), "dungtv");
		assertEquals(object.get("strs", String[].class), new String[] {"a", "b", "c"});
		assertEquals(object.get("byteList",  byte[][].class), new byte[][] {{1, 2,3}});
	}
	
	@Test
	public void test1() {
		EzyObject origin = EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("a", 1)
				.append("b", 2)
				.append("c", 3)
				.build();
		byte[] bytes = serializer.serialize(origin);
		EzyObject after = deserializer.deserialize(bytes);
		assert after.size() == 3;
		assert after.get("a", int.class) == 1;
		assert after.get("b", byte.class) == 2;
		assert after.get("c", short.class) == 3;
	}
	
	@Test
	public void test2() {
		EzyArray origin = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append(1, 2, 3)
				.build();
		byte[] bytes = serializer.serialize(origin);
		EzyArray after = deserializer.deserialize(ByteBuffer.wrap(bytes));
		assert after.size() == 3;
		assert after.get(0, int.class) == 1;
		assert after.get(1, byte.class) == 2;
		assert after.get(2, short.class) == 3;
	}
	
	@Test(expectedExceptions = {InvocationTargetException.class})
	public void test3() throws Exception {
		Method method = JacksonSimpleSerializer
				.class.getDeclaredMethod("writeValueAsBytes", Object.class);
		method.setAccessible(true);
		method.invoke(serializer, new ClassB());
	}
	
	public void test5() {
		EzyArray array = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append((Long)null)
				.append(1L)
				.append(new byte[] {1, 2, 3})
				.build();
		byte[] bytes = serializer.serialize(array);
		EzyArray after = deserializer.deserialize(bytes);
		assertEquals(after.get(0, Long.class), null);
		assertEquals(after.get(1, Long.class), new Long(1L));
		assertEquals(after.get(2), new byte[] {1, 2, 3});
	}
	
	@Data
	public static class ClassA {
		private String name = "abc";
		private long value = 123;
		private String[] parents = {"dungtv", "tuyennc", "hunglq"};
		private boolean yesNo = true;
		private byte[] bytes = new byte[] {1, 2, 3, 4, 5};
		private Long longValue;
		private char[] chs = {'1', '2', '3'};
		private double doubleValue = 123.456D;
		private ClassC classC = new ClassC();
		private List<String> strs = Lists.newArrayList("a", "b", "c");
		private List<byte[]> byteList = Lists.newArrayList(new byte[] {1, 2, 3});
		private List<ClassC> classCs = Lists.newArrayList(new ClassC());
		private byte[] base64Bytes = EzyBase64.encode("base64");
		private String base64String = EzyBase64.encodeUtf("base64");
	}
	
	public static class ClassB {
		
		@JsonProperty("name")
		public String getName() {
			throw new RuntimeException();
		}
	}
	
	@Data
	public static class ClassC {
		private String name = "dungtv"; 
	}
	
}
