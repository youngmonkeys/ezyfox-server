package com.tvd12.ezyfoxserver.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;

/**
 * 
 * @author tavandung12
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
public @interface EzySingleton {

	String value() default "";
	
	EzyKeyValue[] properties() default {};
	
}
