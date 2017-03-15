package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFetchAppByName;
import com.tvd12.ezyfoxserver.config.EzyApp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFetchAppByNameImpl 
		extends EzyAbstractCommand 
		implements EzyFetchAppByName {

	private EzyServer boss;
	
	@Override
	public EzyApp get(String name) {
		return boss.getAppByName(name);
	}
	
}
