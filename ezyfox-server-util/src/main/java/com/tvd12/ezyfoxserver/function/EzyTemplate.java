package com.tvd12.ezyfoxserver.function;

public interface EzyTemplate<I, O> 
		extends EzySerializer<I, O>, EzyDeserializer<O, I> {

}
