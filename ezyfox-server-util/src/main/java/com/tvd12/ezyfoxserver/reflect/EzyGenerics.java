package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class EzyGenerics {

	private EzyGenerics() {
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getOneGenericClassArgument(Type genericType)  {
		return getGenericClassArguments(genericType, 1)[0];
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getTwoGenericClassArguments(Type genericType)  {
		return getGenericClassArguments(genericType, 2);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getGenericClassArguments(Type genericType, int size)  {
		if (!(genericType instanceof ParameterizedType))
			throw new IllegalArgumentException("1: " + genericType.getTypeName() + " is not generic type");
		List<Class> answer = new ArrayList<>();
		Type[] types = ((ParameterizedType)genericType).getActualTypeArguments();
		if(types.length != size)
			throw new IllegalArgumentException("2: " + genericType.getTypeName() + " has != " + size + " generic argument");
		for(Type type : types) {
			if(type instanceof Class)
				answer.add((Class)type);
			else if(type instanceof ParameterizedType)
				answer.add((Class)((ParameterizedType)type).getRawType());
			else
				throw new IllegalArgumentException("3: unknown generic argument type of " + genericType.getTypeName());
		}
		return answer.toArray(new Class[answer.size()]);
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[] getGenericInterfacesArguments(Class<?> clazz, Class<?> interfaceClass, int size) {
		Type[] genericInterfaces = clazz.getGenericInterfaces();
		for (Type genericInterface : genericInterfaces) { 
		    if (!(genericInterface instanceof ParameterizedType))
		    	continue;
		    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
		    if(!parameterizedType.getRawType().equals(interfaceClass))
				continue;
	        Type[] genericTypes = parameterizedType.getActualTypeArguments();
	        if(genericTypes.length != size)
				throw new IllegalArgumentException("1:" + genericInterface.getTypeName() + " has != " + size + " generic argument");
	        List<Class> answer = new ArrayList<>();
	        for (Type genericType : genericTypes) { 
	        	if(genericType instanceof Class)
					answer.add((Class)genericType);
				else if(genericType instanceof ParameterizedType)
					answer.add((Class)((ParameterizedType)genericType).getRawType());
				else
					throw new IllegalArgumentException("2: unknown generic argument type of " + genericType.getTypeName());
	        }
	        return answer.toArray(new Class[answer.size()]);
		} 
		if(EzyInterfaces.getInterface(clazz, interfaceClass) != null)
			throw new IllegalArgumentException("3: " + interfaceClass + " is not generics");
		throw new IllegalArgumentException("4: " + clazz + " is not extends/implements " + interfaceClass);
	}
}
