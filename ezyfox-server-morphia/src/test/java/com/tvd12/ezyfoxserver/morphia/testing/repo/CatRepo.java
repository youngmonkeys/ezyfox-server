package com.tvd12.ezyfoxserver.morphia.testing.repo;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;

@EzyAutoImpl
public interface CatRepo extends EzyMongoRepository<Long, Cat> {
	
}
