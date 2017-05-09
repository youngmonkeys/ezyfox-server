package com.tvd12.ezyfoxserver.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;

import lombok.AllArgsConstructor;

@SuppressWarnings("rawtypes")
@AllArgsConstructor
public class EzyMethodFinder {
	protected Class clazz;
	protected String methodName;
	protected Class[] parameterTypes;
	
	public Method find() {
		return getMethod(clazz);
	}
	
	protected Method getMethod(Class clazz) {
		Method method = tryGetMethod(clazz);
		if(method != null)
			return method;
		Class[] interfaces = getInterfaces(clazz);
		for(Class itf : interfaces) {
			method = getMethod(itf);
			if(method != null)
				return method;
		}
		Class superClass = getSupperClasses(clazz);
		return superClass != null ? getMethod(superClass) : null;
	}
	
	@SuppressWarnings({ "unchecked" })
	protected Method tryGetMethod(Class clazz) {
		try {
			return clazz.getDeclaredMethod(methodName, parameterTypes);
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
	
	public static class Builder implements EzyBuilder<EzyMethodFinder> {
		protected Class clazz;
		protected String methodName;
		protected List<Class> parameterTypes = new ArrayList<>();
		
		public Builder clazz(Class clazz) {
			this.clazz = clazz;
			return this;
		}
		public Builder methodName(String methodName) {
			this.methodName = methodName;
			return this;
		}
		
		public Builder parameterTypes(Class... parameterTypes) {
			this.parameterTypes.addAll(Lists.newArrayList(parameterTypes));
			return this;
		}
		
		@Override
		public EzyMethodFinder build() {
			return new EzyMethodFinder(
					clazz, 
					methodName, 
					parameterTypes.toArray(new Class[parameterTypes.size()]));
		}
	}
	
}
