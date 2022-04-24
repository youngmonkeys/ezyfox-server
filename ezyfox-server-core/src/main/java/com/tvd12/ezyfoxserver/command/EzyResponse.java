package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyResponse extends EzyVoidCommand {

    EzyResponse encrypted();

    EzyResponse encrypted(boolean value);

    EzyResponse command(String command);

    EzyResponse params(EzyData params);

    EzyResponse user(EzyUser user, boolean exclude);

    EzyResponse users(EzyUser[] users, boolean exclude);

    EzyResponse users(Iterable<EzyUser> users, boolean exclude);

    EzyResponse username(String username, boolean exclude);

    EzyResponse usernames(String[] usernames, boolean exclude);

    EzyResponse usernames(Iterable<String> usernames, boolean exclude);

    EzyResponse session(EzySession session, boolean exclude);

    EzyResponse sessions(EzySession[] sessions, boolean exclude);

    EzyResponse sessions(Iterable<EzySession> sessions, boolean exclude);

    EzyResponse transportType(EzyTransportType transportType);

    default EzyResponse user(EzyUser user) {
        return user(user, false);
    }

    default EzyResponse users(EzyUser... users) {
        return users(users, false);
    }

    default EzyResponse users(Iterable<EzyUser> users) {
        return users(users, false);
    }

    default EzyResponse username(String username) {
        return username(username, false);
    }

    default EzyResponse usernames(String... usernames) {
        return usernames(usernames, false);
    }

    default EzyResponse usernames(Iterable<String> usernames) {
        return usernames(usernames, false);
    }

    default EzyResponse session(EzySession session) {
        return session(session, false);
    }

    default EzyResponse sessions(EzySession... sessions) {
        return sessions(sessions, false);
    }

    default EzyResponse sessions(Iterable<EzySession> sessions) {
        return sessions(sessions, false);
    }

    default EzyResponse params(EzyBuilder<? extends EzyData> builder) {
        return params(builder.build());
    }
}
