package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.util.EzyNameAware;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleUser 
        extends EzyEntity 
        implements EzyUser, EzyNameAware, EzyHasUserRemoveDelegate, Serializable {
	private static final long serialVersionUID = -7846882289922504595L;
	
	protected long id = COUNTER.incrementAndGet();
	protected String name = "";
	protected String password = "";
	protected int maxSessions = 30;
	protected long maxIdleTime = 3 * 60 * 1000;
	protected long startIdleTime = System.currentTimeMillis();
	protected transient EzyUserRemoveDelegate removeDelegate;
	@Setter(AccessLevel.NONE)
    protected Map<String, Lock> locks = new ConcurrentHashMap<>();
	@Setter(AccessLevel.NONE)
	protected Map<String, EzySession> sessionMap = new ConcurrentHashMap<>();

	private transient static final AtomicLong COUNTER = new AtomicLong(0);
	
	@Override
	public void addSession(EzySession session) {
	    sessionMap.put(session.getReconnectToken(), session);
	}
	
	@Override
	public void removeSession(EzySession session) {
	    setStartIdleTime(System.currentTimeMillis());
	    sessionMap.remove(session.getReconnectToken());
	}
	
	@Override
	public Collection<EzySession> getSessions() {
	    return new ArrayList<>(sessionMap.values());
	}
	
	@Override
	public int getSessionCount() {
	    return sessionMap.size();
	}
	
	@Override
    public Lock getLock(String name) {
        return locks.computeIfAbsent(name, k -> new ReentrantLock());
    }
	
	@Override
	public void send(EzyData data, EzyTransportType type) {
	    for(EzySession session : getSessions()) {
	        session.send(data, type);
	    }
	}
	
	@Override
	public void sendNow(EzyData data, EzyTransportType type) {
	    for(EzySession session : getSessions()) {
            session.sendNow(data, type);
	    }
	}
	
	@Override
	public boolean isIdle() {
	    if(!sessionMap.isEmpty())
	        return false;
	    return maxIdleTime < System.currentTimeMillis() - startIdleTime;
	}
	
	@Override
	public void destroy() {
	    this.removeDelegate = null;
	    if(locks != null)
	        locks.clear();
	    if(properties != null)
	        this.properties.clear();
	    if(sessionMap != null)
	        this.sessionMap.clear();
	    this.locks = null;
	    this.properties = null;
	    this.sessionMap = null;
	}
	
	@Override
    public boolean equals(Object obj) {
	    return new EzyEquals<EzySimpleUser>()
	            .function(c -> c.id)
	            .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(id).toHashCode();
    }
    
    @Override
    public String toString() {
        return name;
    }
	
}
