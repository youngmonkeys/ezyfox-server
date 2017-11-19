package com.tvd12.ezyfoxserver.admintools.service.impl;

import static com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths.API_THREADS_DETAIL;
import static com.tvd12.ezyfoxserver.admintools.constants.EzyWebApiPaths.API_THREAD_COUNT;

import org.springframework.stereotype.Service;

import com.tvd12.ezyfoxserver.admintools.data.EzyThreadsDetail;
import com.tvd12.ezyfoxserver.admintools.service.EzyThreadsService;
import com.tvd12.ezyfoxserver.admintools.service.EzyWepApiService;

@Service("threadsService")
public class EzyThreadsServiceImpl extends EzyWepApiService implements EzyThreadsService {

	@Override
	public Long getThreadCount() {
		return get(API_THREAD_COUNT, Long.class);
	}
	
	@Override
	public EzyThreadsDetail getThreadsDetail() {
		return get(API_THREADS_DETAIL, EzyThreadsDetail.class);
	}
}
