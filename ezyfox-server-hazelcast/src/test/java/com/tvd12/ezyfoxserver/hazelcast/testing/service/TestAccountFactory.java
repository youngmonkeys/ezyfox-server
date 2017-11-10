package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAbstractAccount;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyAbstractAccoutFactory;

public class TestAccountFactory extends EzyAbstractAccoutFactory {

	@Override
	protected EzyAbstractAccount newAccount() {
		return new TestAcount2();
	}
	
}
