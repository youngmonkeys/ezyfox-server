package com.tvd12.ezyfoxserver.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EzyKeyValue {
	
	String key();
	
	String value();
	
}
