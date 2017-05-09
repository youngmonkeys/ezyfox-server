package com.tvd12.ezyfoxserver.mongodb;

import com.tvd12.ezyfoxserver.database.repository.EzyEmptyRepository;
import com.tvd12.ezyfoxserver.database.service.EzyCrudService;

public interface EzyMongoRepository<I,E> 
		extends EzyEmptyRepository<I, E>, EzyCrudService<I, E> {

}
