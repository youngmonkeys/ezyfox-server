package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.test.base.BaseTest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class EzySimpleBindingContextArrayTest extends BaseTest {

	@Test
	public void test() {
		EzyArrayWriterBuilder.setDebug(true);
		EzyArrayReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addArrayBindingClass(A1.class)
				.addArrayBindingClass(A2.class)
				.addArrayBindingClass(A3.class)
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		EzyArray array = marshaller.marshal(new A1());
		A1 a = unmarshaller.unmarshal(array, A1.class);
		System.out.println(a);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test1() {
		EzyArrayWriterBuilder.setDebug(true);
		EzyArrayReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addArrayBindingClass(A1.class)
				.addArrayBindingClass(A2.class)
				.addArrayBindingClass(A3.class)
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		EzyArray array = marshaller.marshal(new A2());
		A2 a = unmarshaller.unmarshal(array, A2.class);
		System.out.println(a);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		EzyArrayWriterBuilder.setDebug(true);
		EzyArrayReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addArrayBindingClass(A1.class)
				.addArrayBindingClass(A2.class)
				.addArrayBindingClass(A3.class)
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		marshaller.marshal(new A3());
	}
	
	@Getter
	@Setter
	@ToString
	public static class A1 {
		private String name  = "n";
		private String value = "v";
	}
	
	@Getter
	@Setter
	@ToString
	@EzyArrayBinding(read = false)
	public static class A2 {
		private String name  = "n";
		private String value = "v";
	}
	
	@Getter
	@Setter
	@ToString
	@EzyArrayBinding(write = false)
	public static class A3 {
		private String name  = "n";
		private String value = "v";
	}
	
}
