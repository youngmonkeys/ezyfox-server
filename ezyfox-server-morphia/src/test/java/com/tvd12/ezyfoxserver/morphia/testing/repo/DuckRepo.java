package com.tvd12.ezyfoxserver.morphia.testing.repo;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.testing.Duck;

@EzyAutoImpl
public interface DuckRepo extends EzyMongoRepository<Long, Duck> {
	
}
