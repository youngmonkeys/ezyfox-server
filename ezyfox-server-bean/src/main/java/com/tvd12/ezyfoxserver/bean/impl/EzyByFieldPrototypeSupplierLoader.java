package com.tvd12.ezyfoxserver.bean.impl;

import java.util.List;
import java.util.Map;

import com.tvd12.ezyfoxserver.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.reflect.EzyField;

public class EzyByFieldPrototypeSupplierLoader 
		extends EzySimplePrototypeSupplierLoader
		implements EzyPrototypeSupplierLoader {

	protected final EzyField field;
	protected final Object configurator;
	
	public EzyByFieldPrototypeSupplierLoader(EzyField field, Object configurator) {
		super(new EzyClass(field.getType()));
		this.field = field;
		this.configurator = configurator;
	}
	
	@Override
	protected String getPrototypeName() {
		return EzyBeanNameParser.getPrototypeName(
				field.getAnnotation(EzyPrototype.class), field.getName());
	}
	
	@Override
	protected Class<?>[] getConstructorParameterTypes() {
		return new Class[0];
	}
	
	@Override
	protected String[] getConstructorArgumentNames() {
		return new String[0];
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Map getAnnotationProperties() {
		return EzyKeyValueParser.getPrototypeProperties(
				field.getAnnotation(EzyPrototype.class));
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
				.append(field.getName());
		return instruction;
	}
	
}
