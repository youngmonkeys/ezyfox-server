package com.tvd12.ezyfoxserver.codec;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tvd12.ezyfoxserver.entity.EzyObject;

public class JacksonObjectSerializer extends StdSerializer<EzyObject> {
	private static final long serialVersionUID = 1033303749441882688L;
	
	public JacksonObjectSerializer(Class<EzyObject> t) {
		super(t);
	}

	@Override
	public void serialize(EzyObject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.getCodec().writeValue(gen, value.toMap());
	}
	
}
