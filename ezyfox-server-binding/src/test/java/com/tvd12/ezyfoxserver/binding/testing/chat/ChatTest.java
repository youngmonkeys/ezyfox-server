package com.tvd12.ezyfoxserver.binding.testing.chat;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;

public class ChatTest {

	@Test
	public void test() {
		EzyBindingContext context = EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.chat")
				.build();
		EzyMarshaller marshaller = context.newMarshaller();
		Object object = marshaller.marshal(new ChatMessage());
		System.out.println(object);
	}
	
}
