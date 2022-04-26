package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.pattern.EzyObjectProvider;
import com.tvd12.ezyfox.util.EzyTimes;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.exception.EzyMaxSessionException;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.socket.EzyChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class EzySimpleSessionManager<S extends EzySession>
    extends EzyObjectProvider<S>
    implements EzySessionManager<S> {

    protected final int maxSessions;
    protected final EzySessionTokenGenerator tokenGenerator;
    protected final ConcurrentHashMap<Long, S> loggedInSession
        = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<Long, S> sessionsById
        = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<Object, S> sessionsByConnection
        = new ConcurrentHashMap<>();

    protected static final AtomicInteger COUNTER
        = new AtomicInteger(0);

    protected EzySimpleSessionManager(Builder<S> builder) {
        super(builder);
        this.maxSessions = builder.maxSessions;
        this.tokenGenerator = builder.tokenGenerator;
    }

    @Override
    public void addLoggedInSession(S session) {
        loggedInSession.put(session.getId(), session);
    }

    @Override
    public boolean containsSession(long id) {
        return sessionsById.containsKey(id);
    }

    @Override
    public void removeSession(S session, EzyConstant reason) {
        checkToRemoveSession(session, reason);
    }

    @Override
    public void clearSession(S session) {
        if (session != null) {
            unmapSession(session);
            logger.info(getRemoveSessionMessage(session));
        }
    }

    private String getRemoveSessionMessage(S session) {
        return "remove session: " + session.getName() +
            ", remain sessions = " + providedObjects.size() +
            ", login sessions = " + loggedInSession.size() +
            ", sessions by id = " + sessionsById.size() +
            ", sessions by connection = " + sessionsByConnection.size();
    }

    protected void checkToRemoveSession(S session, EzyConstant reason) {
        if (shouldRemoveSession(session)) {
            session.disconnect(reason);
        }
    }

    protected boolean shouldRemoveSession(S session) {
        if (session == null) {
            return false;
        }
        return containsSession(session.getId());
    }

    protected void unmapSession(S session) {
        providedObjects.remove(session);
        sessionsById.remove(session.getId());
        loggedInSession.remove(session.getId());
        EzyChannel channel = session.getChannel();
        Object connection = channel.getConnection();
        sessionsByConnection.remove(connection);
    }

    @Override
    public S provideSession(EzyChannel channel) {
        S ss = provideSession(channel.getConnectionType());
        ss.setChannel(channel);
        sessionsByConnection.put(channel.getConnection(), ss);
        logger.info(
            "provide session: {}, sessions size = {}",
            ss.getName(),
            providedObjects.size()
        );
        return ss;
    }

    @SuppressWarnings("unchecked")
    protected S provideSession(EzyConstant connectionType) {
        checkMaxSessions();
        EzyAbstractSession session = (EzyAbstractSession) provideObject();
        session.setLoggedIn(false);
        session.setName("Session#" + COUNTER.incrementAndGet());
        session.setConnectionType(connectionType);
        session.setToken(newSessionToken());
        session.setCreationTime(System.currentTimeMillis());

        session.setCreationTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastWriteTime(System.currentTimeMillis());
        S complete = (S) session;
        sessionsById.put(complete.getId(), complete);
        return complete;
    }

    protected void checkMaxSessions() {
        int current = providedObjects.size();
        if (current >= maxSessions) {
            throw new EzyMaxSessionException(current, maxSessions);
        }
    }

    @Override
    public EzySession getSession(long id) {
        return sessionsById.get(id);
    }

    @Override
    public S getSession(Object connection) {
        return sessionsByConnection.get(connection);
    }

    @Override
    public List<S> getAllSessions() {
        return getProvidedObjects();
    }

    @Override
    public List<S> getAliveSessions() {
        return new ArrayList<>(sessionsById.values());
    }

    @Override
    public List<S> getLoggedInSessions() {
        return new ArrayList<>(loggedInSession.values());
    }

    @Override
    public int getAllSessionCount() {
        return providedObjects.size();
    }

    @Override
    public int getAliveSessionCount() {
        return sessionsById.size();
    }

    @Override
    public int getLoggedInSessionCount() {
        return loggedInSession.size();
    }

    @Override
    public void start() throws Exception {
        super.start();
        logger.debug("start session manager");
    }

    @Override
    protected void removeStaleObjects(List<S> buffer) {
        checkAndRemoveSessions(buffer);
    }

    protected void checkAndRemoveSessions(List<S> sessions) {
        List<S> idleSessions = new ArrayList<>();
        List<S> unloggedInSessions = new ArrayList<>();
        for (S session : sessions) {
            boolean unloggedIn = isUnloggedInSession(session);
            if (unloggedIn) {
                unloggedInSessions.add(session);
                continue;
            }
            boolean idle = isIdleSession(session);
            if (idle) {
                idleSessions.add(session);
            }
        }
        for (S session : idleSessions) {
            removeSession(session, EzyDisconnectReason.IDLE);
        }
        for (S session : unloggedInSessions) {
            removeSession(session, EzyDisconnectReason.NOT_LOGGED_IN);
        }
    }

    protected boolean isIdleSession(S session) {
        return session.isIdle();
    }

    protected boolean isUnloggedInSession(EzySession session) {
        if (session.isLoggedIn()) {
            return false;
        }
        if (!session.isActivated()) {
            return false;
        }
        long remainTime = getSessionRemainWaitingTime(session);
        return remainTime <= 0;
    }

    protected long getSessionRemainWaitingTime(EzySession session) {
        long maxWaitingTime = session.getMaxWaitingTime();
        long creationTime = session.getCreationTime();
        return EzyTimes.getRemainTime(maxWaitingTime, creationTime);
    }

    protected String newSessionToken() {
        return tokenGenerator.generate();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.loggedInSession.clear();
        this.sessionsById.clear();
    }

    public abstract static class Builder<S extends EzySession>
        extends EzyObjectProvider.Builder<S, Builder<S>> {

        protected int maxSessions = 999999;
        protected EzySessionTokenGenerator tokenGenerator;

        @Override
        protected String getValidationThreadPoolName() {
            return "session-manager";
        }

        public Builder<S> maxSessions(int maxSessions) {
            this.maxSessions = maxSessions;
            return this;
        }

        public Builder<S> tokenGenerator(EzySessionTokenGenerator generator) {
            this.tokenGenerator = generator;
            return this;
        }

        public abstract EzySimpleSessionManager<S> build();
    }
}
