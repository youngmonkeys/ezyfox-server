package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyVoidCommand;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyResponse<T extends EzyResponse<T>> extends EzyVoidCommand {

    T encrypted();

    T encrypted(boolean value);

    T data(Object data);

    T command(String command);
    
    T user(EzyUser user, boolean exclude);
    
    T users(EzyUser[] users, boolean exclude);
    
    T users(Iterable<EzyUser> users, boolean exclude);
    
    T session(EzySession session, boolean exclude);
    
    T sessions(EzySession[] sessions, boolean exclude);
    
    T sessions(Iterable<EzySession> sessions, boolean exclude);
    
    T username(String username, boolean exclude);
    
    T usernames(String[] usernames, boolean exclude);
    
    T usernames(Iterable<String> usernames, boolean exclude);
    
    T transportType(EzyTransportType transportType);
    
    default T data(EzyBuilder<?> builder) {
        return data(builder.build());
    }
    
    default T user(EzyUser user) {
            return user(user, false);
    }
    
    default T users(EzyUser... users) {
            return users(users, false);
    }
    
    default T users(Iterable<EzyUser> users) {
            return users(users, false);
    }
    
    default T session(EzySession session) {
            return session(session, false);
    }
    
    default T sessions(EzySession... sessions) {
            return sessions(sessions, false);
    }
    
    default T sessions(Iterable<EzySession> sessions) {
            return sessions(sessions, false);
    }
    
    default T username(String username) {
            return username(username, false);
    }
    
    default T usernames(String... usernames) {
            return usernames(usernames, false);
    }
    
    default T usernames(Iterable<String> usernames) {
            return usernames(usernames, false);
    }
    
    default T udpTransport() {
        return transportType(EzyTransportType.UDP);
    }
}
