package com.tvd12.ezyfoxserver.binding.testing.objectbinding;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.test.base.BaseTest;

public class BindingTest extends BaseTest {

	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.objectbinding")
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		
		EzyObject object = marshaller.marshal(new ClassF());
		System.out.println(object);
	}
	
}
