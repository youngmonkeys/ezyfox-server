package com.tvd12.ezyfoxserver.wrapper.impl;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public class SimpleUserManager implements EzyUserManager {

	private ConcurrentHashMap<Long, EzyUser> usersById;
	private ConcurrentHashMap<String, EzyUser> usersByName;
	private ConcurrentHashMap<EzySession, EzyUser> usersBySession;
	
	public SimpleUserManager() {
		this.usersById = new ConcurrentHashMap<>();
		this.usersByName = new ConcurrentHashMap<>();
		this.usersBySession = new ConcurrentHashMap<>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#addUser(com.tvd12.ezyfoxserver.entity.EzyUser)
	 */
	@Override
	public void addUser(EzyUser user) {
		usersById.putIfAbsent(user.getId(), user);
		usersByName.putIfAbsent(user.getName(), user);
		usersBySession.putIfAbsent(user.getSession(), user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#getUser(long)
	 */
	@Override
	public EzyUser getUser(long userId) {
		return usersById.get(userId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#getUser(java.lang.String)
	 */
	@Override
	public EzyUser getUser(String username) {
		return usersByName.get(username);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#getUser(com.tvd12.ezyfoxserver.entity.EzySession)
	 */
	@Override
	public EzyUser getUser(EzySession session) {
		return usersBySession.get(session);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#containsUser(long)
	 */
	@Override
	public boolean containsUser(long userId) {
		return usersById.containsKey(userId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#containsUser(java.lang.String)
	 */
	@Override
	public boolean containsUser(String username) {
		return usersByName.containsKey(username);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#containsUser(com.tvd12.ezyfoxserver.entity.EzySession)
	 */
	@Override
	public boolean containsUser(EzySession session) {
		return usersBySession.containsKey(session);
	}

	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.wrapper.EzyUserManager#removeUser(com.tvd12.ezyfoxserver.entity.EzyUser)
	 */
	@Override
	public void removeUser(EzyUser user) {
		if(user != null) {
			usersById.remove(user.getId());
			usersByName.remove(user.getName());
			usersBySession.remove(user.getSession());
		}
	}
	
	
	
	
}
