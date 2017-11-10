package com.tvd12.ezyfoxserver.binding.testing.unwrapper;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnwrapper;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectUnwrapperBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzyObjectUnwrapperBuilderTest extends BaseTest {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.build();
		EzyObjectUnwrapperBuilder.setDebug(true);
		EzyObjectUnwrapperBuilder builder = 
				new EzyObjectUnwrapperBuilder(new EzyClass(ClassA.class));
		EzyObject object = EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("a", "hi!")
				.build();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		EzyUnwrapper unwrapper = builder.build();
		ClassA classA = new ClassA();
		unwrapper.unwrap(unmarshaller, object, classA);
		System.out.println(classA);
	}
	
}
