package com.tvd12.ezyfoxserver.binding.testing.unwrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectUnwrapperBuilder;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.test.base.BaseTest;

public class EzyUnwrapperTest extends BaseTest {

	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.unwrapper")
				.build();
		EzyObjectUnwrapperBuilder.setDebug(true);
		EzyArray array = EzyEntityFactory.create(EzyArrayBuilder.class)
				.append("hehe")
				.build();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		ClassA classA = new ClassA();
		unmarshaller.unwrap(array, classA);
		System.out.println(classA);
	}
	
}
