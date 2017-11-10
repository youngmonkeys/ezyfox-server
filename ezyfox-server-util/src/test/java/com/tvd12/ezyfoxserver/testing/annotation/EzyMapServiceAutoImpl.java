package com.tvd12.ezyfoxserver.testing.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
@EzyAutoImpl(properties = @EzyKeyValue(key = "map-name", value = ""))
public @interface EzyMapServiceAutoImpl {

}
