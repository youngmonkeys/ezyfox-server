package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyControllers {

	EzyController<EzyData> getController(EzyConstant cmd);
	
}
