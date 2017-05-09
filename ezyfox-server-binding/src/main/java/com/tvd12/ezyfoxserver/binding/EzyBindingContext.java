package com.tvd12.ezyfoxserver.binding;

import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;

@SuppressWarnings("rawtypes")
public interface EzyBindingContext {
	
	EzyMarshaller newMarshaller();
	
	EzyUnmarshaller newUnmarshaller();
	
	void addReader(EzyReader reader);
	
	void addWriter(EzyWriter writer);
	
	void addTemplate(Object template);
	
	void bindReader(Class clazz, EzyReader reader);
	
	void bindWriter(Class clazz, EzyWriter writer);
	
	void bindTemplate(Class clazz, Object template);
	
	static EzyBindingContextBuilder builder() {
		return EzySimpleBindingContext.builder(); 
	}
}
