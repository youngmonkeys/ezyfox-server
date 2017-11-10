package com.tvd12.ezyfoxserver.bean.exception;

import com.tvd12.ezyfoxserver.bean.impl.EzyBeanKey;

import lombok.Getter;

@Getter
public class EzyNewSingletonException extends IllegalStateException {
	private static final long serialVersionUID = -1494071992523176740L;

	private final String errorBeanName;
	private final Class<?> errorClass;
	private final Class<?> singletonClass;
	
	public EzyNewSingletonException(
			Class<?> singletonClass, Class<?> errorClass, String errorBeanName) {
		super(
			"can't load singleton of class " + 
			singletonClass.getSimpleName() + 
			", can't set (" + errorBeanName + ", " + errorClass.getSimpleName() + ")"
		);
		this.errorClass = errorClass;
		this.singletonClass = singletonClass;
		this.errorBeanName = errorBeanName;
	}
	
	public final EzyBeanKey getErrorKey() {
		return EzyBeanKey.of(getErrorBeanName(), getErrorClass());
	}

}
