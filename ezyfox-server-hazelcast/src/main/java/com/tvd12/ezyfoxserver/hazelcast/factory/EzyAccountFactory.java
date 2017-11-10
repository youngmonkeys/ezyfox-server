package com.tvd12.ezyfoxserver.hazelcast.factory;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;

public interface EzyAccountFactory {

	EzyAccount newAccount(long accountId, boolean acceptNegativeValue);
	
	default EzyAccount newAccount(long accountId) {
		return newAccount(accountId, false);
	}
	
}
