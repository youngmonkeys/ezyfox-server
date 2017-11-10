package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.Field;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import lombok.AllArgsConstructor;

@SuppressWarnings("rawtypes")
@AllArgsConstructor
public class EzyFieldFinder {
	protected Class clazz;
	protected String fieldName;
	
	public Field find() {
		return getField(clazz);
	}
	
	protected Field getField(Class clazz) {
		Field field = tryGetField(clazz);
		if(field != null)
			return field;
		Class[] interfaces = getInterfaces(clazz);
		for(Class itf : interfaces) {
			field = getField(itf);
			if(field != null)
				return field;
		}
		Class superClass = getSupperClasses(clazz);
		return superClass != null ? getField(superClass) : null;
	}
	
	protected Field tryGetField(Class clazz) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected Class getSupperClasses(Class clazz) {
		return clazz.getSuperclass();
	}
	
	protected Class[] getInterfaces(Class clazz) {
		return clazz.getInterfaces();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyFieldFinder> {
		protected Class clazz;
		protected String fieldName;
		
		public Builder clazz(Class clazz) {
			this.clazz = clazz;
			return this;
		}
		public Builder fieldName(String fieldName) {
			this.fieldName = fieldName;
			return this;
		}
		
		@Override
		public EzyFieldFinder build() {
			return new EzyFieldFinder(clazz, fieldName);
		}
	}
	
}
