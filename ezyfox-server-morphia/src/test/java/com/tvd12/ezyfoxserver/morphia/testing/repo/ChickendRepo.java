package com.tvd12.ezyfoxserver.morphia.testing.repo;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.testing.data.Chickend;

@EzyAutoImpl
public interface ChickendRepo extends EzyMongoRepository<Long, Chickend> {
	
}
