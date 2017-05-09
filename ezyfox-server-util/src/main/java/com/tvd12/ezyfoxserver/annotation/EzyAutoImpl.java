package com.tvd12.ezyfoxserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EzyAutoImpl {

	String value() default "";
	
	EzyKeyValue[] properties() default {};
	
}
