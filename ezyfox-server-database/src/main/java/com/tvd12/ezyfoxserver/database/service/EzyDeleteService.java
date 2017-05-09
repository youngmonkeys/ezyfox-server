package com.tvd12.ezyfoxserver.database.service;

public interface EzyDeleteService<I> extends
		EzyDeleteAllService,
		EzyDeleteByIdService<I>, 
		EzyDeleteByIdsService<I> {

}
