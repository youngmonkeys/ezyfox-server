package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyVoidCommand;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyResponse<T extends EzyResponse<T>> extends EzyVoidCommand {

    /**
     * Indicates response data will be encrypted.
     *
     * @return this pointer.
     */
    T encrypted();

    /**
     * Indicates response data will be encrypted or not.
     *
     * @param value true is encrypted, false is not encrypted.
     * @return this pointer.
     */
    T encrypted(boolean value);

    /**
     * Set data to response.
     *
     * @param data the data.
     * @return this pointer.
     */
    T data(Object data);

    /**
     * Set data to response from a builder object by call <code>builder.build()</code>.
     *
     * @param builder the builder object.
     * @return this pointer.
     */
    default T data(EzyBuilder<?> builder) {
        return data(builder.build());
    }

    /**
     * Set response command.
     *
     * @param command the response command.
     * @return this pointer.
     */
    T command(String command);

    /**
     * Includes/excludes a user to/from response receiver list.
     *
     * @param user the user.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T user(EzyUser user, boolean exclude);

    /**
     * Includes a user to response receiver list.
     *
     * @param user the user.
     * @return this pointer.
     */
    default T user(EzyUser user) {
        return user(user, false);
    }

    /**
     * Includes/excludes an array of users to/from response receiver list.
     *
     * @param users the array of users.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T users(EzyUser[] users, boolean exclude);

    /**
     * Includes/excludes a collection of users to/from response receiver list.
     *
     * @param users the collection of users.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T users(Iterable<EzyUser> users, boolean exclude);

    /**
     * Includes an array of user to response receiver list.
     *
     * @param users the array of users.
     * @return this pointer.
     */
    default T users(EzyUser... users) {
        return users(users, false);
    }

    /**
     * Includes a collection of users to response receiver list.
     *
     * @param users the collection of users.
     * @return this pointer.
     */
    default T users(Iterable<EzyUser> users) {
        return users(users, false);
    }

    /**
     * Includes/excludes a session to/from response receiver list.
     *
     * @param session the session.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T session(EzySession session, boolean exclude);

    /**
     * Includes a session to response receiver list.
     *
     * @param session the session.
     * @return this pointer.
     */
    default T session(EzySession session) {
        return session(session, false);
    }

    /**
     * Includes/excludes an array of sessions to/from response receiver list.
     *
     * @param sessions the array of sessions.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T sessions(EzySession[] sessions, boolean exclude);

    /**
     * Includes/excludes a collection of sessions to/from response receiver list.
     *
     * @param sessions the collection of sessions.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T sessions(Iterable<EzySession> sessions, boolean exclude);

    /**
     * Includes an array sessions to response receiver list.
     *
     * @param sessions the array of sessions.
     * @return this pointer.
     */
    default T sessions(EzySession... sessions) {
        return sessions(sessions, false);
    }

    /**
     * Includes a collection of sessions to response receiver list.
     *
     * @param sessions the collection of sessions.
     * @return this pointer.
     */
    default T sessions(Iterable<EzySession> sessions) {
        return sessions(sessions, false);
    }

    /**
     * Includes/excludes a user to/from response receiver list by username.
     *
     * @param username the username of the user.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T username(String username, boolean exclude);

    /**
     * Includes a user to response receiver list by username.
     *
     * @param username the username of the user.
     * @return this pointer.
     */
    default T username(String username) {
        return username(username, false);
    }

    /**
     * Includes/excludes an array of users to/from response receiver list by username.
     *
     * @param usernames the array of usernames of the users.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T usernames(String[] usernames, boolean exclude);

    /**
     * Includes/excludes an collection of users to/from response receiver list by username.
     *
     * @param usernames the collection of usernames of the users.
     * @param exclude false is inclusive, true is exclusive.
     * @return this pointer.
     */
    T usernames(Iterable<String> usernames, boolean exclude);

    /**
     * Includes an array of users to response receiver list by username.
     *
     * @param usernames the array of usernames of the users.
     * @return this pointer.
     */
    default T usernames(String... usernames) {
        return usernames(usernames, false);
    }

    /**
     * Includes an collection of users to response receiver list by username.
     *
     * @param usernames the collection of usernames of the users.
     * @return this pointer.
     */
    default T usernames(Iterable<String> usernames) {
        return usernames(usernames, false);
    }

    /**
     * Set transport type to response.
     *
     * @param transportType the transport type.
     * @return this pointer.
     */
    T transportType(EzyTransportType transportType);

    /**
     * Set transport type to response is UDP.
     *
     * @return this pointer.
     */
    default T udpTransport() {
        return transportType(EzyTransportType.UDP);
    }

    /**
     * Set transport type to response is UDP or TCP if UDP is not available.
     *
     * @return this pointer.
     */
    default T udpOrTcpTransport() {
        return transportType(EzyTransportType.UDP_OR_TCP);
    }
}
