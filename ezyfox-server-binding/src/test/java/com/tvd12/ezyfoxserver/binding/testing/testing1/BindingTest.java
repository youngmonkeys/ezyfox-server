package com.tvd12.ezyfoxserver.binding.testing.testing1;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.test.base.BaseTest;

public class BindingTest extends BaseTest {

	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.testing1")
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyArray array = marshaller.marshal(new ClassA());
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		InterfaceA interfaceA = unmarshaller.unmarshal(array, InterfaceA.class);
		System.out.println(interfaceA);
	}
	
}
