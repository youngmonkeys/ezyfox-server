package com.tvd12.ezyfoxserver.bean.testing.prototype1;

import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPrototype;

@EzyPrototype(value = "hello", properties = {
		@EzyKeyValue(key = "type", value = "request_listener"),
		@EzyKeyValue(key = "cmd", value = "2"),
		@EzyKeyValue(key = "priority", value = "1")
})
public class ClassB {

}
