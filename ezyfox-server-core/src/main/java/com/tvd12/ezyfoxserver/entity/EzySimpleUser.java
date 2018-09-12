package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfox.util.EzyNameAware;
import com.tvd12.ezyfoxserver.setting.EzyZoneIdAware;
import com.tvd12.ezyfoxserver.socket.EzyPacket;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleUser 
        extends EzyEntity 
        implements EzyUser, EzyNameAware, EzyZoneIdAware, Serializable {
	private static final long serialVersionUID = -7846882289922504595L;
	
	protected long id = COUNTER.incrementAndGet();
	protected String name = "";
	protected String password = "";
	protected int zoneId = 0;
	protected int maxSessions = 30;
	protected long maxIdleTime = 3 * 60 * 1000;
	protected long startIdleTime = System.currentTimeMillis();
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
	    sessionMap.remove(session.getReconnectToken());
	}
	
	@Override
	public EzySession getSession() {
	    List<EzySession> sessions = getSessions();
	    return sessions.isEmpty() ? null : sessions.get(0);
	}
	
	@Override
	public List<EzySession> getSessions() {
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
	public void send(EzyPacket packet) {
	    for(EzySession session : getSessions())
	        session.send(packet);
	}
	
	@Override
	public void sendNow(EzyPacket packet) {
	    for(EzySession session : getSessions())
            session.sendNow(packet);
	}
	
	@Override
	public boolean isIdle() {
	    if(!sessionMap.isEmpty())
	        return false;
	    boolean idle = maxIdleTime < System.currentTimeMillis() - startIdleTime;
	    return idle;
	}
	
	@Override
	public void disconnect(EzyConstant reason) {
	    for(EzySession session : getSessions())
	        session.disconnect(reason);
	}
	
	@Override
	public void destroy() {
	    if(locks != null)
	        locks.clear();
	    if(properties != null)
	        this.properties.clear();
	    if(sessionMap != null)
	        this.sessionMap.clear();
	    this.locks = null;
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
