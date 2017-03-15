package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFetchServer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFetchServerImpl implements EzyFetchServer {

	private EzyServer boss;
	
	@Override
	public EzyServer get() {
		return boss;
	}

}
