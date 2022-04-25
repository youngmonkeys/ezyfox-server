package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfox.concurrent.EzyExecutors;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.function.EzyFunctions;
import com.tvd12.ezyfox.util.EzyStartable;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.delegate.EzyUserDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyAbstractUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzyZoneUserManagerImpl
    extends EzyAbstractUserManager
    implements EzyZoneUserManager, EzyStartable {

    protected final String zoneName;
    protected final EzyUserDelegate userDelegate;
    protected final long idleValidationDelay;
    protected final long idleValidationInterval;
    protected final int idleValidationThreadPoolSize;
    protected final ScheduledExecutorService idleValidationService;
    protected final ConcurrentHashMap<EzySession, EzyUser> usersBySession = new ConcurrentHashMap<>();

    protected EzyZoneUserManagerImpl(Builder builder) {
        super(builder);
        this.zoneName = builder.zoneName;
        this.userDelegate = builder.userDelegate;
        this.idleValidationDelay = builder.idleValidationDelay;
        this.idleValidationInterval = builder.idleValidationInterval;
        this.idleValidationThreadPoolSize = builder.idleValidationThreadPoolSize;
        this.idleValidationService = newIdleValidationService(builder.maxIdleTime);
    }

    public static Builder builder() {
        return new Builder();
    }

    protected ScheduledExecutorService newIdleValidationService(long maxIdleTime) {
        if (maxIdleTime <= 0) {
            return null;
        }
        ScheduledExecutorService answer = EzyExecutors.newScheduledThreadPool(idleValidationThreadPoolSize, "user-manager");
        Runtime.getRuntime().addShutdownHook(new Thread(answer::shutdown));
        return answer;
    }

    /*
     * (non-Javadoc)
     * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#addUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser)
     */
    @Override
    public void addUser(EzySession session, EzyUser user) {
        checkMaxUsers();
        usersById.put(user.getId(), user);
        usersByName.put(user.getName(), user);
        usersBySession.put(session, user);
        logger.info("zone: {} add user: {}, locks.size = {}, usersById.size = {}, usersByName.size = {}", zoneName, user, locks.size(), usersById.size(), usersByName.size());
    }

    @Override
    public void bind(EzySession session, EzyUser user) {
        usersBySession.put(session, user);
    }

    /*
     * (non-Javadoc)
     * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#getUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
     */
    @Override
    public EzyUser getUser(EzySession session) {
        return usersBySession.get(session);
    }

    /*
     * (non-Javadoc)
     * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyUserManager#containsUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
     */
    @Override
    public boolean containsUser(EzySession session) {
        return usersBySession.containsKey(session);
    }

    /*
     * (non-Javadoc)
     * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyServerUserManager#unmapSessionUser(com.tvd12.ezyfoxserver.mapping.entity.EzySession)
     */
    @Override
    public void unmapSessionUser(EzySession session, EzyConstant reason) {
        EzyUser user = usersBySession.remove(session);
        if (user != null) {
            user.removeSession(session);
            logger.debug("zone: {} remove session {} from user {} by reason {}, user remain: {} sessions, usersBySession.size: {}", zoneName, session.getClientAddress(), user, reason, user.getSessionCount(), usersBySession.size());
            if (shouldRemoveUserNow(user)) {
                removeUser(user, reason);
            }
        }
    }

    protected boolean shouldRemoveUserNow(EzyUser user) {
        int sessionCount = user.getSessionCount();
        long maxIdleTime = user.getMaxIdleTime();
        return sessionCount <= 0 && maxIdleTime <= 0;
    }

    /*
     * (non-Javadoc)
     * @see com.tvd12.ezyfoxserver.mapping.wrapper.EzyServerUserManager#removeUser(com.tvd12.ezyfoxserver.mapping.entity.EzyUser, com.tvd12.ezyfoxserver.mapping.constant.EzyUserRemoveReason)
     */
    @Override
    public void removeUser(EzyUser user, EzyConstant reason) {
        String username = user.getName();
        Lock lock = locks.computeIfAbsent(username, EzyFunctions.NEW_REENTRANT_LOCK_FUNC);
        lock.lock();
        try {
            removeUser0(user, reason);
        } finally {
            lock.unlock();
            locks.remove(username);
        }
    }

    private void removeUser0(EzyUser user, EzyConstant reason) {
        logger.debug("zone: {} remove user: {} by reason: {}", zoneName, user, reason);
        removeUser(user);
        delegateUserRemove(user, reason);
    }

    @Override
    public void start() throws Exception {
        logger.debug("start user manager for zone: {}", zoneName);
        startIdleValidationService();
    }

    protected void startIdleValidationService() {
        if (idleValidationService != null) {
            idleValidationService.scheduleAtFixedRate(
                this::validateIdleUsers,
                idleValidationDelay,
                idleValidationInterval, TimeUnit.MILLISECONDS);
        }
    }

    protected void validateIdleUsers() {
        List<EzyUser> toRemoveUsers = new ArrayList<>();
        for (EzyUser user : getUserList()) {
            if (isIdleUser(user)) {
                toRemoveUsers.add(user);
            }
        }
        for (EzyUser user : toRemoveUsers) {
            removeUser(user, EzyDisconnectReason.IDLE);
        }
    }

    protected boolean isIdleUser(EzyUser user) {
        return user.isIdle();
    }

    protected void delegateUserRemove(EzyUser user, EzyConstant reason) {
        userDelegate.onUserRemoved(user, reason);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.usersBySession.clear();
        if (idleValidationService != null) {
            processWithLogException(idleValidationService::shutdown);
        }
    }

    @Override
    protected String getMessagePrefix() {
        return "zone: " + zoneName;
    }

    public static class Builder extends EzyAbstractUserManager.Builder<Builder> {

        protected String zoneName;
        protected long maxIdleTime;
        protected EzyUserDelegate userDelegate;
        protected long idleValidationInterval = 100;
        protected long idleValidationDelay = 3 * 1000;
        protected int idleValidationThreadPoolSize = 1;

        public Builder zoneName(String zoneName) {
            this.zoneName = zoneName;
            return this;
        }

        public Builder maxIdleTime(long maxIdleTime) {
            this.maxIdleTime = maxIdleTime;
            return this;
        }

        public Builder userDelegate(EzyUserDelegate userDelegate) {
            this.userDelegate = userDelegate;
            return this;
        }

        public Builder idleValidationDelay(long validationDelay) {
            this.idleValidationDelay = validationDelay;
            return this;
        }

        public Builder idleValidationInterval(long validationInterval) {
            this.idleValidationInterval = validationInterval;
            return this;
        }

        public Builder idleValidationThreadPoolSize(int threadPoolSize) {
            this.idleValidationThreadPoolSize = threadPoolSize;
            return this;
        }

        @Override
        public EzyZoneUserManager build() {
            return new EzyZoneUserManagerImpl(this);
        }

    }
}
