package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Set;

import com.tvd12.ezyfoxserver.function.EzyValidator;

public class EzyGenericSetterValidator implements EzyValidator<Type> {

	@Override
	public boolean validate(Type genericType) {
		if(genericType instanceof WildcardType)
			return false;
		if(genericType instanceof Class)
			return validateClassType(genericType);
		if(genericType instanceof ParameterizedType)
			return validate((ParameterizedType)genericType);
		return false;
	}
	
	protected boolean validate(ParameterizedType parameterizedType) {
		Type[] types = parameterizedType.getActualTypeArguments();
		for(Type type : types) {
			if(!validate(type)) 
				return false;
		}
		return true;
	}
	
	protected boolean validateClassType(Type classType) {
		return !getCommonGenericTypes().contains(classType);
	}
	
	@SuppressWarnings("rawtypes")
	protected Set<Class> getCommonGenericTypes() {
		return EzyTypes.COMMON_GENERIC_TYPES;
	}
	
}
