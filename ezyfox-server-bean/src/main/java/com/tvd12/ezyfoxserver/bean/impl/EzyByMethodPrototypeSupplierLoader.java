package com.tvd12.ezyfoxserver.bean.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;

public class EzyByMethodPrototypeSupplierLoader 
		extends EzySimplePrototypeSupplierLoader
		implements EzyPrototypeSupplierLoader {

	protected final EzyMethod method;
	protected final Object configurator;
	
	public EzyByMethodPrototypeSupplierLoader(EzyMethod method, Object configurator) {
		super(new EzyClass(method.getReturnType()));
		this.method = method;
		this.configurator = configurator;
	}
	
	@Override
	protected String getPrototypeName() {
		return EzyBeanNameParser.getPrototypeName(
				method.getAnnotation(EzyPrototype.class), method.getFieldName());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Map getAnnotationProperties() {
		return EzyKeyValueParser.getPrototypeProperties(
				method.getAnnotation(EzyPrototype.class));
	}
	
	@Override
	protected Class<?>[] getConstructorParameterTypes() {
		return method.getParameterTypes();
	}
	
	@Override
	protected EzyInstruction newConstructInstruction(EzyBody body, List<String> cparams) {
		Class<?> configClass = configurator.getClass();
		EzyInstruction prepare = newVariableInstruction(
				configClass, "configurator", EzyClasses.getVariableName(configClass));
		body.append(prepare);
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.variable(clazz.getClazz(), "object")
				.equal()
				.append("configurator.")
				.append(method.getName())
				.bracketopen()
				.append(StringUtils.join(cparams, ", "))
				.bracketclose();
		return instruction;
	}
	
}
