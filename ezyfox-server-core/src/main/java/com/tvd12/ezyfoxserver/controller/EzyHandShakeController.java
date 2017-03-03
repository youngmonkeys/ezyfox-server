package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzyHandShakeController extends EzyLoggable implements EzyByArrayCotroller {

	@Override
	public void handle(int appId, EzyArray data) {
		getLogger().info("hanshake handler");
	}

}
