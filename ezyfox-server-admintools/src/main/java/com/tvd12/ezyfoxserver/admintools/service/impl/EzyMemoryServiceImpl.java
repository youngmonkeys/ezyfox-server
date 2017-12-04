package com.tvd12.ezyfoxserver.admintools.service.impl;

import org.springframework.stereotype.Service;

import com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths;
import com.tvd12.ezyfoxserver.admintools.data.EzyMemoryPoint;
import com.tvd12.ezyfoxserver.admintools.service.EzyMemoryService;
import com.tvd12.ezyfoxserver.admintools.service.EzyWepApiService;

@Service("memoryService")
public class EzyMemoryServiceImpl extends EzyWepApiService implements EzyMemoryService {

	@Override
	public EzyMemoryPoint getMemoryDetails() {
		return get(EzyWebApiPaths.API_MEMORY_DETAIL, EzyMemoryPoint.class);
	}

}
