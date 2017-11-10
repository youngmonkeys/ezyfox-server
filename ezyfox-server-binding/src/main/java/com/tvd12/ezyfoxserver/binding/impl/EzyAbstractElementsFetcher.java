package com.tvd12.ezyfoxserver.binding.impl;

import static com.tvd12.ezyfoxserver.binding.EzyAccessType.DECLARED_FIELDS;
import static com.tvd12.ezyfoxserver.binding.EzyAccessType.DECLARED_METHODS;
import static com.tvd12.ezyfoxserver.binding.EzyAccessType.FIELDS;
import static com.tvd12.ezyfoxserver.binding.EzyAccessType.METHODS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tvd12.ezyfoxserver.binding.annotation.EzyIgnore;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyByFieldMethod;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

/**
 * 
 * @author tavandung12
 *
 */

public abstract class EzyAbstractElementsFetcher 
		extends EzyLoggable 
		implements EzyElementsFetcher {
	
	protected final List<EzyField> getFields(EzyClass clazz, int accessType) {
		List<EzyField> fields = new ArrayList<>();
		if((accessType & FIELDS) > 0) {
			fields.addAll(clazz.getFields());
		}
		else if((accessType & DECLARED_FIELDS) > 0) {
			fields.addAll(clazz.getDeclaredFields());
		}
		return EzyLists.filter(fields, f -> !f.isAnnotated(EzyIgnore.class));
	}
	
	protected final List<? extends EzyMethod> getMethods(EzyClass clazz, int accessType) {
		List<EzyMethod> methods = new ArrayList<>();
		if((accessType & METHODS) > 0) {
			methods.addAll(getMethods(clazz));
		}
		else if((accessType & DECLARED_METHODS) > 0) {
			methods.addAll(getDeclaredMethods(clazz));
		}
		return EzyLists.filter(methods, m -> !isIgnoredMethod(m, clazz));
	}
	
	protected boolean isValidGenericField(EzyField field) {
		return true;
	}
	protected boolean isValidGenericMethod(EzyMethod method) {
		return true;
	}
	
	protected abstract List<? extends EzyMethod> getMethods(EzyClass clazz);
	protected abstract List<? extends EzyMethod> getDeclaredMethods(EzyClass clazz);
	
	protected final Map<String, ? extends EzyMethod> getMethodsByFieldName(EzyClass clazz) {
		return mapMethodsByFieldName(getMethods(clazz));
	}
	
	private final Map<String, ? extends EzyMethod> 
			mapMethodsByFieldName(List<? extends EzyMethod> methods) {
		return EzyMaps.newHashMap(methods, m -> ((EzyByFieldMethod)m).getFieldName());
	}
	
	private boolean isIgnoredMethod(EzyMethod method, EzyClass clazz) {
		boolean ignored = method.isAnnotated(EzyIgnore.class);
		if(ignored) return true;
		String fieldName = ((EzyByFieldMethod)method).getFieldName();
		EzyField field = clazz.getField(fieldName);
		return field == null ? false : field.isAnnotated(EzyIgnore.class);
	}
}