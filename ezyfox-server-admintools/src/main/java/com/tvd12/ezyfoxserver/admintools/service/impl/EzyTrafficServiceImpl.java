package com.tvd12.ezyfoxserver.admintools.service.impl;

import static com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths.API_TRAFFIC_DETAILS;

import org.springframework.stereotype.Service;

import com.tvd12.ezyfoxserver.admintools.data.EzyTrafficDetails;
import com.tvd12.ezyfoxserver.admintools.service.EzyTrafficService;
import com.tvd12.ezyfoxserver.admintools.service.EzyWepApiService;

@Service("trafficService")
public class EzyTrafficServiceImpl extends EzyWepApiService implements EzyTrafficService{

	@Override
	public EzyTrafficDetails getTrafficDetails() {
		return get(API_TRAFFIC_DETAILS, EzyTrafficDetails.class);
	}
	

}
