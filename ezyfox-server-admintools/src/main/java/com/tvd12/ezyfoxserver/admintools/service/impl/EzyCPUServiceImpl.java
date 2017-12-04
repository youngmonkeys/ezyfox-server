package com.tvd12.ezyfoxserver.admintools.service.impl;

import org.springframework.stereotype.Service;

import com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths;
import com.tvd12.ezyfoxserver.admintools.data.EzyCpuPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyCPUService;
import com.tvd12.ezyfoxserver.admintools.service.EzyWepApiService;

@Service("cpuService")
public class EzyCPUServiceImpl extends EzyWepApiService implements EzyCPUService {

	@Override
	public EzyCpuPoint getCPUDetails() {
		return get(EzyWebApiPaths.API_CPU_DETAIL, EzyCpuPoint.class);
	}

}
