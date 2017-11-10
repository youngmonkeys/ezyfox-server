package com.tvd12.ezyfoxserver.binding.testing.scan1;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.binding.testing.scan1.ClassA.ClassB;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class Scan1Example2 {

	public static void main(String[] args) throws Exception {
		new Scan1Example2().test();
	}
	
	@SuppressWarnings("rawtypes")
	public void test() {
		
		EzyObjectReaderBuilder.setDebug(true);
		EzyArrayReaderBuilder.setDebug(true);
		EzyObjectWriterBuilder.setDebug(true);
		EzyArrayWriterBuilder.setDebug(true);
		
		EzySimpleBindingContext context = EzySimpleBindingContext.builder()
				.addClass(ClassA.class)
				.addClass(ClassB.class)
				.build();
		
		EzyMarshaller marshaller = context.newMarshaller();
		EzyObject outObject = marshaller.marshal(new ClassA());
		System.out.println(outObject);
		outObject.put("hung", "xau trai");
		EzyUnmarshaller unmarshaller = context.newUnmarshaller();
		ClassA outEntity = unmarshaller.unmarshal(outObject, ClassA.class);
		System.out.println(outEntity);
	}
}
