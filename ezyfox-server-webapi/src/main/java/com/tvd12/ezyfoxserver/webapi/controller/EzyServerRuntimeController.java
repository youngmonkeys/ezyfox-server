package com.tvd12.ezyfoxserver.webapi.controller;

import com.tvd12.ezyfoxserver.databridge.proxy.EzyProxyTime;

public class EzyServerRuntimeController extends EzyStatisticsController {

	public EzyProxyTime getSetverSetting() {
		return new EzyProxyTime(getStatistics().getStartTime());
	}
	
}
