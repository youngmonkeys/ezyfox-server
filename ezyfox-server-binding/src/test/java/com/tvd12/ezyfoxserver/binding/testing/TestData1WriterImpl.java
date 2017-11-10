package com.tvd12.ezyfoxserver.binding.testing;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriterImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractWriter;
import com.tvd12.ezyfoxserver.entity.EzyArray;

@EzyWriterImpl
public class TestData1WriterImpl
		extends EzyAbstractWriter<EzyTestData1, EzyArray> {

	@Override
	public EzyArray write(EzyMarshaller marshaller, EzyTestData1 object) {
		return newArrayBuilder()
				.append(object.getData1())
				.append(object.getData2())
				.append(object.getData3())
				.build();
	}
	
}
