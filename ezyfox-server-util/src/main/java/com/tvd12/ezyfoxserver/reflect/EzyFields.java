package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Lists;

public final class EzyFields {

	private EzyFields() {
	}
	
	public static Object get(Field field, Object obj) {
		try {
			return field.get(obj);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't get value from field " + field.getName(), e);
		}
	}
	
	public static void set(Field field, Object obj, Object value) {
		try {
			field.set(obj, value);
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't set value to field " + field.getName(), e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Field> getFields(Class clazz) {
		return Lists.newArrayList(FieldUtils.getAllFields(clazz));
	}
	
	@SuppressWarnings("rawtypes")
	public static Field getField(Class clazz, String fieldName) {
		return new EzyFieldFinder(clazz, fieldName).find();
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Field> getAnnotatedFields(
			Class clazz, Class<? extends Annotation> annClass) {
		return FieldUtils.getFieldsListWithAnnotation(clazz, annClass);
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Field> getDeclaredFields(Class clazz) {
		return Lists.newArrayList(clazz.getDeclaredFields());
	}
	
}
