package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyEventType;

public class EzyEventControllerSettingBuilder 
		implements EzyBuilder<EzySimpleEventControllerSetting> {

	private String eventType;
	private String controller;
	
	public EzyEventControllerSettingBuilder eventType(EzyEventType eventType) {
		this.eventType = eventType.toString();
		return this;
	}
	
	public EzyEventControllerSettingBuilder controller(Class<?> controllerClass) {
		this.controller = controllerClass.getName();
		return this;
	}
	
	@Override
	public EzySimpleEventControllerSetting build() {
		EzySimpleEventControllerSetting setting = new EzySimpleEventControllerSetting();
		setting.setEventType(eventType);
		setting.setController(controller);
		return setting;
	}

}
