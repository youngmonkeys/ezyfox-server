package com.tvd12.ezyfoxserver.binding.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tvd12.ezyfoxserver.binding.EzyAccessType;

/**
 * @author tavandung12
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface EzyObjectBinding {
	
	/**
	 * @return make reader implementation nor not
	 */
	boolean read() default true;
	
	/**
	 * @return make writer implementation nor not
	 */
	boolean write() default true;
	
	/**
	 * @return binding all sub types
	 */
	boolean subTypes() default false;
	
	/**
	 * @return the access type
	 */
	int accessType() default EzyAccessType.ALL;
	
	/**
	 * @return sub type classes to include
	 */
	Class<?>[] subTypeClasses() default {};
	
}
