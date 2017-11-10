package com.tvd12.ezyfoxserver.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author tavandung12
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface EzyValue {

	/**
	 * the key mapped to value
	 * 
	 * @return the key mapped to value
	 */
	public String value() default "";

}
