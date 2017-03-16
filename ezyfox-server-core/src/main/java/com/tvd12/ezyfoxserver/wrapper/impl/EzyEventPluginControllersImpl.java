package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfoxserver.wrapper.EzyAbstractEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

public class EzyEventPluginControllersImpl extends EzyAbstractEventControllers {

	protected EzyEventPluginControllersImpl(Builder builder) {
		super(builder);
	}

	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEventControllers.Builder {
		
		@Override
		public EzyEventControllers build() {
			return new EzyEventPluginControllersImpl(this);
		}
	}

}
