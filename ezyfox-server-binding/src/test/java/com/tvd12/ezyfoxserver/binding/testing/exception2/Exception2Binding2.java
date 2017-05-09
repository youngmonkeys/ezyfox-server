package com.tvd12.ezyfoxserver.binding.testing.exception2;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

public class Exception2Binding2 extends EzyEntityBuilders {

	public static void main(String[] args) throws Exception {
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClass(Exception2ClassB.class)
				.build();
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		Exception2ClassB answer = unmarshaller.unmarshal(
				EzyEntityFactory.create(EzyObjectBuilder.class)
				.append("value", "abc")
				.build(), 
				Exception2ClassB.class);
		System.out.println(answer);
	}
}
