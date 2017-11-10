package com.tvd12.ezyfoxserver.binding.testing.arraybinding;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyArrayWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleMarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleUnmarshaller;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

public class EzyArrayWriterBuilderExample1 {

	@Test
	@SuppressWarnings("unchecked")
	public void test() throws Exception {
		EzyMarshaller marshaller = new EzySimpleMarshaller();
		EzyArrayWriterBuilder.setDebug(true);
		EzyArrayWriterBuilder writerBuilder 
				= new EzyArrayWriterBuilder(new EzyClass(ClassA.class));
		EzyWriter<ClassA, EzyArray> writer = writerBuilder.build();
		EzyArray array = writer.write(marshaller, new ClassA());
		System.out.println(array);
		
		EzyUnmarshaller unmarshaller = new EzySimpleUnmarshaller();
		EzyArrayReaderBuilder.setDebug(true);
		EzyArrayReaderBuilder readerBuilder 
				= new EzyArrayReaderBuilder(new EzyClass(ClassA.class));
		EzyReader<EzyArray, ClassA> reader = readerBuilder.build();
		ClassA classA = reader.read(unmarshaller, array);
		System.out.println(classA);
	}
}
