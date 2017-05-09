package com.tvd12.ezyfoxserver.morphia.testing.repo1;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.testing.Pig;

@EzyAutoImpl
public interface PigRepo extends EzyMongoRepository<Long, Pig> {
	
}
