package com.tvd12.ezyfoxserver.binding.testing.scan;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.testing.scan.pack1.ClassB;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.test.base.BaseTest;

public class BindingTest extends BaseTest {

	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.scan.pack0")
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		
		EzyObject object = marshaller.marshal(new ClassB());
		System.out.println(object);
		ClassB classB = unmarshaller.unmarshal(object, ClassB.class);
		System.out.println(classB);
	}
	
}
