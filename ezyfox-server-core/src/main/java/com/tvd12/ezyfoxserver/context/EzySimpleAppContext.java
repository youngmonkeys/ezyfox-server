package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.config.EzyApp;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAppContext extends EzyBaseContext implements EzyAppContext {

	@Setter
	@Getter
	protected EzyApp app;
	@Setter
	protected EzyContext parent;
	
	@Override
	public <T> T get(Class<T> clazz) {
		return parent.get(clazz);
	}

}
