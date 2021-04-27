package com.tvd12.ezyfoxserver.support.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestAppSingletonController;

public abstract class EzyDefaultAppEntry extends EzySimpleAppEntry {

	@Override
	protected EzyAppRequestController newUserRequestController(EzyBeanContext beanContext) {
		return EzyUserRequestAppSingletonController.builder()
				.beanContext(beanContext)
				.build();
	}
}
