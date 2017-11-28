package com.tvd12.ezyfoxserver.binding.testing.scan.pack1;

import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReaderImpl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
public class ClassB {

	@com.tvd12.ezyfoxserver.binding.annotation.EzyReader(TypeReader.class)
	private Type type = Type.HELLO;
	
	public static enum Type {
		HELLO
	}
	
	@EzyReaderImpl
	public static class TypeReader implements EzyReader<Object, Type> {
		@Override
		public Type read(EzyUnmarshaller unmarshaller, Object value) {
			return Type.valueOf(value.toString());
		}
	}
	
}
