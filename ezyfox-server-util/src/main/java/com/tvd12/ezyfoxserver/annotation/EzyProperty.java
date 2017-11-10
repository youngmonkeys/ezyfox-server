package com.tvd12.ezyfoxserver.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ 
	ElementType.FIELD, 
	ElementType.METHOD 
})
@Retention(RetentionPolicy.RUNTIME)
public @interface EzyProperty {
	
	String value() default "";
	
}
