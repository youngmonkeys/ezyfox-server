package com.tvd12.ezyfoxserver.entity;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.constant.EzyHasName;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyProperties;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;

import java.util.List;
import java.util.concurrent.locks.Lock;

public interface EzyUser extends EzyDeliver, EzyHasName, EzyProperties, EzyDestroyable {

    /**
     * Get user id.
     *
     * @return the user id
     */
    long getId();

    /**
     * Get zone id.
     *
     * @return the zone id
     */
    int getZoneId();

    /**
     * Get max session.
     *
     * @return the max session
     */
    int getMaxSessions();

    /**
     * Get max idle time.
     *
     * @return the max idle time
     */
    long getMaxIdleTime();

    /**
     * Get start idle time.
     *
     * @return the start idle time
     */
    long getStartIdleTime();

    /**
     * Set start idle time.
     *
     * @param time the start idle time
     */
    void setStartIdleTime(long time);

    /**
     * Get the session count.
     *
     * @return the session count
     */
    int getSessionCount();

    /**
     * Get the first session.
     *
     * @return the first session
     */
    EzySession getSession();

    /**
     * Get current session.
     *
     * @return the current session
     */
    List<EzySession> getSessions();

    /**
     * Add new session.
     *
     * @param session the session to add
     */
    void addSession(EzySession session);

    /**
     * remove a session.
     *
     * @param session the session to remove
     */
    void removeSession(EzySession session);

    /**
     * change current session.
     *
     * @param session the session to change
     * @return the old sessions
     */
    List<EzySession> changeSession(EzySession session);

    /**
     * The user is idle or not.
     *
     * @return is idle or not
     */
    boolean isIdle();

    /**
     * The user is destroyed or not.
     *
     * @return destroyed or not
     */
    boolean isDestroyed();

    /**
     * Get the lock of the user.
     *
     * @param name the lock name
     * @return the lock
     */
    Lock getLock(String name);

    /**
     * disconnect.
     *
     * @param reason the reason
     */
    void disconnect(EzyConstant reason);

    /**
     * disconnect.
     */
    default void disconnect() {
        disconnect(EzyDisconnectReason.UNKNOWN);
    }
}
