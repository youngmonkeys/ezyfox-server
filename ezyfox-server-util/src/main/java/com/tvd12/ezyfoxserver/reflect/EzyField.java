package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;

public class EzyField 
		implements EzyReflectElement, EzyGenericElement, EzyKnownTypeElement {

	@Getter
	protected final Field field;
	
	public EzyField(Field field) {
		this.field = field;
	}
	
	public Object get(Object obj) {
		return EzyFields.get(field, obj);
	}
	
	public void set(Object obj, Object value) {
		EzyFields.set(field, obj, value);
	}
	
	@Override
	public String getName() {
		return field.getName();
	}
	
	public boolean isPublic() {
		return Modifier.isPublic(field.getModifiers());
	}
	
	public boolean isWritable() {
		return !Modifier.isFinal(field.getModifiers());
	}
	
	public boolean isMapType() {
		return Map.class.isAssignableFrom(getType());
	}
	
	public boolean isCollection() {
		return Collection.class.isAssignableFrom(getType());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getType() {
		return field.getType();
	}
	
	public String getGetterMethod() {
		return "get" + getMethodSuffix();
	}
	
	public String getSetterMethod() {
		return "set" + getMethodSuffix();
	}
	
	protected String getMethodSuffix() {
		String name = getName();
		String first = name.substring(0, 1).toUpperCase();
		return name.length() == 1 ? first : first + name.substring(1);
	}
	
	@Override
	public Type getGenericType() {
		return field.getGenericType();
	}
	
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annClass) {
		return field.getAnnotation(annClass);
	}
	
	@Override
	public boolean isAnnotated(Class<? extends Annotation> annClass) {
		return field.isAnnotationPresent(annClass);
	}
	
	@Override
	public boolean equals(Object obj) {
		return new EzyEquals<EzyField>()
				.function(f -> f.field)
				.isEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return new EzyHashCodes()
				.append(field)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return field.toString();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	@SuppressWarnings("rawtypes")
	public static class Builder implements EzyBuilder<EzyField> {
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
		public EzyField build() {
			return new EzyField(getField());
		}
		
		protected Field getField() {
			return EzyFields.getField(clazz, fieldName);
		}
		
	}
	
}
