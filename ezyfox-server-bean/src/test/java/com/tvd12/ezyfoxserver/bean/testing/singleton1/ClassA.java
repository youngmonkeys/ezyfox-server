package com.tvd12.ezyfoxserver.bean.testing.singleton1;

import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

@EzySingleton(properties = {
		@EzyKeyValue(key = "type", value = "request_listener"),
		@EzyKeyValue(key = "cmd", value = "1"),
		@EzyKeyValue(key = "priority", value = "1")
})
public class ClassA {

}
