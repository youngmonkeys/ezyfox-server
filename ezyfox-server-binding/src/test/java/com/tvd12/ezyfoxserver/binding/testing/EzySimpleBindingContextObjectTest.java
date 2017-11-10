package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.test.base.BaseTest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class EzySimpleBindingContextObjectTest extends BaseTest {

	@Test
	public void test() {
		EzyObjectWriterBuilder.setDebug(true);
		EzyObjectReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClasses(A1.class, A2.class, A3.class)
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		EzyObject object = marshaller.marshal(new A1());
		A1 a = unmarshaller.unmarshal(object, A1.class);
		System.out.println(a);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test1() {
		EzyObjectWriterBuilder.setDebug(true);
		EzyObjectReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClasses(A1.class, A2.class, A3.class)
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		EzyObject object = marshaller.marshal(new A2());
		A2 a = unmarshaller.unmarshal(object, A2.class);
		System.out.println(a);
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void test2() {
		EzyObjectWriterBuilder.setDebug(true);
		EzyObjectReaderBuilder.setDebug(true);
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClasses(A1.class, A2.class, A3.class)
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
	@EzyObjectBinding(read = false)
	public static class A2 {
		private String name  = "n";
		private String value = "v";
	}
	
	@Getter
	@Setter
	@ToString
	@EzyObjectBinding(write = false)
	public static class A3 {
		private String name  = "n";
		private String value = "v";
	}
	
}
