package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.binding.testing.scan1.Scan1ClassA;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class EzyPointWriterExample10 {

	@Test
	public void test() throws Exception {
		
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.binding.testing.scan1")
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		EzyObject outObject = marshaller.marshal(new Scan1ClassA());
		outObject.put("hung", "xau trai");
		System.out.println(outObject);
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		Scan1ClassA outEntity = unmarshaller.unmarshal(outObject, Scan1ClassA.class);
		System.out.println(outEntity);
	}
}
