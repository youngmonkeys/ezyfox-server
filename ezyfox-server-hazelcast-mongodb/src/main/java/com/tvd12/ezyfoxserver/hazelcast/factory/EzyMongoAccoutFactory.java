package com.tvd12.ezyfoxserver.hazelcast.factory;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyMongoAccount;

public class EzyMongoAccoutFactory extends EzyAbstractAccoutFactory {

	@Override
	protected EzyMongoAccount newAccount() {
		return new EzyMongoAccount();
	}
	
}
