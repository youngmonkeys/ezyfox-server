package com.tvd12.ezyfoxserver.admintools.service;

import com.tvd12.ezyfoxserver.admintools.data.EzyThreadsDetail;

public interface EzyThreadsService {

	public Long getThreadCount();
	
	public EzyThreadsDetail getThreadsDetail();

}
