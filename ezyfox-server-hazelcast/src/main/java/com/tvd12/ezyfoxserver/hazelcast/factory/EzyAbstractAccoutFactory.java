package com.tvd12.ezyfoxserver.hazelcast.factory;

import java.util.Date;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAbstractAccount;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public abstract class EzyAbstractAccoutFactory 
		extends EzyLoggable 
		implements EzyAccountFactory {

	@Override
	public final EzyAccount newAccount(long accountId, boolean acceptNegativeValue) {
		EzyAbstractAccount account = newAccount();
		account.setId(accountId);
		account.setCreationDate(new Date());
		account.setLastUpdatedDate(new Date());
		account.setAcceptNegativeValue(acceptNegativeValue);
		return account;
	}

	protected abstract EzyAbstractAccount newAccount();

}
