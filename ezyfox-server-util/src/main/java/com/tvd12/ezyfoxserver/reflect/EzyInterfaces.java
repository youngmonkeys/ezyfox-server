package com.tvd12.ezyfoxserver.reflect;

import java.util.Set;

public final class EzyInterfaces {

	private EzyInterfaces() {
	}
	
	@SuppressWarnings("rawtypes")
	public static Class getInterface(Class clazz, Class interfaceClass) {
		Set<Class> interfaces = EzyClasses.flatInterfaces(clazz);
		for(Class itf : interfaces) {
			if(itf.equals(interfaceClass))
				return itf;
		}
		return null;
			
	}
	
}
