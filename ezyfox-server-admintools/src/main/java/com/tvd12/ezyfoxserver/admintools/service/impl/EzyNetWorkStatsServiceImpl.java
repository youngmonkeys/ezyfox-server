package com.tvd12.ezyfoxserver.admintools.service.impl;

import org.springframework.stereotype.Service;

import com.tvd12.ezyfoxserver.admintools.data.EzyNetworkPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyNetWorkStatsService;
import com.tvd12.ezyfoxserver.admintools.service.EzyWepApiService;

import static com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths.*;

@Service("networkStatsService")
public class EzyNetWorkStatsServiceImpl extends EzyWepApiService implements EzyNetWorkStatsService {

	@Override
	public EzyNetworkPoint getNetWorkStats() {
		return get(API_NETWORK_READ_WRITE, EzyNetworkPoint.class);
	}

}
