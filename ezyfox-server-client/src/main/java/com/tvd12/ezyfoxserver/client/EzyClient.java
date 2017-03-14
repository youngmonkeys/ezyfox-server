package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

import lombok.Getter;

public class EzyClient {

	@Getter
	protected EzyControllers controllers;
	
	{
		controllers = newControllers();
	}
	
	protected EzyControllers newControllers() {
		return EzyClientControllers.builder().build();
	}
	
}
