package com.tvd12.ezyfoxserver.client.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzyClientControllers extends EzyDestroyable {
	
	Object getController(EzyConstant cmd);
	
	void addController(EzyConstant cmd, Object ctrl);
	
}
