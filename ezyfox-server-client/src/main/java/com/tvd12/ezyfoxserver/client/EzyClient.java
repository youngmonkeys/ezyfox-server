package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.entity.EzyDestroyable;
import com.tvd12.ezyfoxserver.wrapper.EzyControllers;

import lombok.Getter;

public class EzyClient implements EzyDestroyable {

	@Getter
	protected EzyControllers controllers;
	
	{
		controllers = newControllers();
	}
	
	protected EzyControllers newControllers() {
		return EzyClientControllers.builder().build();
	}
	
	@Override
	public void destroy() {
		
	}
	
}
