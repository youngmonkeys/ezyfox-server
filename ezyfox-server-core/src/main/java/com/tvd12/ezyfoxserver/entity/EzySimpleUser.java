package com.tvd12.ezyfoxserver.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyEntity;
import com.tvd12.ezyfox.function.EzyFunctions;
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
    protected volatile boolean destroyed = false;
    @Setter(AccessLevel.NONE)
    protected Map<String, Lock> locks = new ConcurrentHashMap<>();
    @Setter(AccessLevel.NONE)
    protected Map<Long, EzySession> sessionMap = new ConcurrentHashMap<>();

    private transient static final AtomicLong COUNTER = new AtomicLong(0);

    @Override
    public void addSession(EzySession session) {
        sessionMap.put(session.getId(), session);
    }

    @Override
    public void removeSession(EzySession session) {
        startIdleTime = System.currentTimeMillis();
        sessionMap.remove(session.getId());
    }

    @Override
    public List<EzySession> changeSession(EzySession session) {
        Map<Long, EzySession> newSessionMap = new ConcurrentHashMap<>();
        newSessionMap.put(session.getId(), session);
        Map<Long, EzySession> oldSessionMap = sessionMap;
        this.sessionMap = newSessionMap;
        List<EzySession> answer = new ArrayList<>(oldSessionMap.values());
        oldSessionMap.clear();
        return answer;
    }

    @Override
    public EzySession getSession() {
        List<EzySession> sessions = getSessions();
        EzySession session = sessions.isEmpty() ? null : sessions.get(0);
        return session;
    }

    @Override
    public List<EzySession> getSessions() {
        List<EzySession> sessions = new ArrayList<>();
        Map<Long, EzySession> sessionMapNow = sessionMap;
        if(sessionMapNow != null)
            sessions.addAll(sessionMapNow.values());
        return sessions;
    }

    @Override
    public int getSessionCount() {
        int size = sessionMap.size();
        return size;
    }

    @Override
    public Lock getLock(String name) {
        Lock lock = locks.computeIfAbsent(name, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
        return lock;
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
        if(sessionMap.isEmpty()) {
            long offset = System.currentTimeMillis() - startIdleTime;
            boolean idle = maxIdleTime < offset;
            return idle;
        }
        return false;
    }

    @Override
    public void disconnect(EzyConstant reason) {
        for(EzySession session : getSessions())
            session.disconnect(reason);
    }

    @Override
    public void destroy() {
        this.destroyed = true;
        if(locks != null)
            locks.clear();
        if(sessionMap != null)
            this.sessionMap.clear();
        this.properties.clear();
        this.locks = null;
        this.sessionMap = null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof EzySimpleUser)
            return id == ((EzySimpleUser)obj).id;
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
    
    @Override
    public String toString() {
        return name;
    }

}
