package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyAddEventController;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.setting.EzyEventControllerAdder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyAddEventControllerImpl 
		extends EzyAbstractCommand
		implements EzyAddEventController {

	private EzyEventControllerAdder ctrlAdder;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void add(EzyConstant eventType, EzyEventController ctrl) {
		ctrlAdder.addEventController(eventType, ctrl);
	}
	
}
