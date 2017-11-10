package com.tvd12.ezyfoxserver.hazelcast.service;

import java.util.Map;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccount;
import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAccountTypeAware;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzyAccountFactory;

import lombok.Setter;

public class EzySimpleAccountService 
		extends EzySimpleHazelcastMapService<Long, EzyAccount>
		implements EzyAccountService, EzyMaxIdServiceAware {
	
	@Setter
	protected EzyMaxIdService maxIdService;
	@Setter
	protected EzyAccountFactory accountFactory;
	
	public EzySimpleAccountService() {
	}
	
	public EzySimpleAccountService(HazelcastInstance hazelcastInstance) {
		super(hazelcastInstance);
	}

	@Override
	public EzyAccount newAccount(String accountType) {
		EzyAccount account = createAccount(accountType);
		map.set(account.getId(), account);
		return account;
	}
	
	@Override
	public EzyAccount getAccount(long accoutId) {
		return get(accoutId);
	}
	
	@Override
	public Map<Long, EzyAccount> getAccounts(Iterable<Long> keys) {
		return getMapByIds(keys);
	}
	
	@Override
	public EzyAccount removeAccount(long accoutId) {
		return remove(accoutId);
	}
	
	@Override
	public void removeAccounts(Iterable<Long> keys) {
		remove(keys);
	}
	
	@Override
	public Long update(long accountId, long offset) {
        try {
            return transactionUpdateAndGet(
            		accountId, 
            		account -> updateAccount(account, offset), 
            		null);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("cannot update accout id = " + accountId + ", with offset = " + offset, e);
        }
	}
	
	@Override
	public Long[] update(long accountId, double percent, long initValue) {
        try {
        	return transactionUpdateAndGet(
        		accountId, 
        		account -> updateAccount(account, percent, initValue), 
        		null);
        }
        catch(Exception e) {
            throw new IllegalArgumentException("cannot update accout id = " + accountId + ", with percent = " + percent, e);
        }
	}
	
	private Long updateAccount(EzyAccount account, long offset) {
        long updated = account.update(offset);
        map.set(account.getId(), account);
        return updated;
	}
	
	private Long[] updateAccount(EzyAccount account, double percent, long initValue) {
        Long[] updated = account.update(percent, initValue);
        map.set(account.getId(), account);
        return updated;
	}
	
	private EzyAccount createAccount(String accountType) {
		long accountId = newAccountId();
		EzyAccount account = accountFactory.newAccount(accountId);
		((EzyAccountTypeAware)account).setType(accountType);
		return account;
	}
	
	private long newAccountId() {
		return maxIdService.incrementAndGet(getIdKeyName());
	}
	
	protected String getIdKeyName() {
		return getMapName();
	}
	
	@Override
	protected String getMapName() {
		return EzyMapNames.ACCOUNT;
	}
	
}
