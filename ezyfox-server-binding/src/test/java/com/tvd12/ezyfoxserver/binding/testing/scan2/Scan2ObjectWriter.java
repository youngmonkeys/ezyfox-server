package com.tvd12.ezyfoxserver.binding.testing.scan2;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriterImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractWriter;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;

@EzyWriterImpl
public class Scan2ObjectWriter extends EzyAbstractWriter<Scan2Object, EzyObject> {

	@Override
	public EzyObject write(EzyMarshaller marshaller, Scan2Object object) {
		EzyObjectBuilder builder = newObjectBuilder();
		return builder.build();
	}
	
}
