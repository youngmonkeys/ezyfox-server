package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.binding.testing.scan1.Scan1ClassA;

public class EzyPointWriterExample6 {

	@Test
	public void test() throws Exception {
		
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.scan1")
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		
		System.out.println(marshaller.marshal(new Scan1ClassA()).toString());
	}
}
