package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class JacksonArraySerializer extends StdSerializer<EzyArray> {
	private static final long serialVersionUID = 47227884568344818L;
	
	public JacksonArraySerializer(Class<EzyArray> t) {
		super(t);
	}

	@Override
	public void serialize(EzyArray value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.getCodec().writeValue(gen, value.toList());
	}
	
	

}
