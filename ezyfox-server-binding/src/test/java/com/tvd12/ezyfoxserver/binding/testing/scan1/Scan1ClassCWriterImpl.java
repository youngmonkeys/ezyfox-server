package com.tvd12.ezyfoxserver.binding.testing.scan1;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriterImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractWriter;
import com.tvd12.ezyfoxserver.entity.EzyArray;

@EzyWriterImpl
public class Scan1ClassCWriterImpl
		extends EzyAbstractWriter<Scan1ClassC, EzyArray> {

	public EzyArray write(EzyMarshaller marshaller, Scan1ClassC object) {
		return newArrayBuilder()
				.append(object.getX())
				.append(object.getY())
				.append(object.getZ())
				.build();
	}
	
}
