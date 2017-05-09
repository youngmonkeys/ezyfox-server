package com.tvd12.ezyfoxserver.hazelcast.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
public @interface EzyMapServiceAutoImpl {

	/**
	 * @return the map name
	 */
	String value();
	
	/**
	 * 
	 * @return the bean name
	 */
	String name() default "";
	
}
