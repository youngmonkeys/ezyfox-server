package com.tvd12.ezyfoxserver.hazelcast.service;

import java.util.Map;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;

public interface EzyAccountService {

	EzyAccount newAccount(String accountType);
	
	EzyAccount getAccount(long accoutId);
	
	Long update(long accountId, long value);

	Long[] update(long accountId, double percent, long initValue);
	
	Map<Long, EzyAccount> getAccounts(Iterable<Long> keys);
	
	EzyAccount removeAccount(long accoutId);
	
	void removeAccounts(Iterable<Long> keys);
	
	default Long[] update(long accountId, double percent) {
		return update(accountId, percent, 0L);
	}
	
}
