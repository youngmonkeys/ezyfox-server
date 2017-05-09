package com.tvd12.ezyfoxserver.morphia.testing.repo1;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.testing.Pig;

@EzyAutoImpl
public abstract class PigRepo2 implements EzyMongoRepository<Long, Pig> {
	
}
