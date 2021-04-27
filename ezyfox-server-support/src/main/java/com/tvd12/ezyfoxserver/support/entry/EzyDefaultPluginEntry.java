package com.tvd12.ezyfoxserver.support.entry;

import com.tvd12.ezyfox.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginSingletonController;

public abstract class EzyDefaultPluginEntry extends EzySimplePluginEntry {

	@Override
	protected EzyPluginRequestController newUserRequestController(EzyBeanContext beanContext) {
		return EzyUserRequestPluginSingletonController.builder()
				.beanContext(beanContext)
				.build();
	}
}
