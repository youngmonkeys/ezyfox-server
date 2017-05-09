package com.tvd12.ezyfoxserver.binding.impl;

import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnwrapper;
import com.tvd12.ezyfoxserver.reflect.EzyClass;

@SuppressWarnings("rawtypes")
public class EzyArrayUnwrapperBuilder extends EzyArrayReaderBuilder {

	protected static AtomicInteger COUNT = new AtomicInteger(0);

	public EzyArrayUnwrapperBuilder(EzyClass clazz) {
		super(clazz);
	}
	
	@Override
	protected String getReadMethodName() {
		return "unwrap";
	}
	
	@Override
	protected Class<?> getReaderInterface() {
		return EzyUnwrapper.class;
	}
	
	@Override
	protected Class[] getReaderMethodParameterTypes() {
		return new Class[] {EzyUnmarshaller.class, Object.class, Object.class};
	}
	
	@Override
	protected EzyInstruction newOutputObjectInstruction() {
		return new EzyInstruction("", "", false)
				.cast(clazz.getClazz(), "arg2");
	}
	
	@Override
	protected String getImplClassName() {
		return clazz.getName() + "$EzyArrayUnwrapper$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
}