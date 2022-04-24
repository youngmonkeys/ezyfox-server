package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

import java.util.HashSet;
import java.util.Set;

public abstract class EzyAbstractResponse<C extends EzyZoneChildContext>
    extends EzyMessageController
    implements EzyResponse, EzyDestroyable {

    protected String command;
    protected EzyData params;
    protected boolean encrypted;
    protected EzyTransportType transportType;
    protected Set<EzySession> recipients = new HashSet<>();
    protected Set<EzySession> exrecipients = new HashSet<>();

    protected C context;
    protected EzyUserManager userManager;

    public EzyAbstractResponse(C context) {
        this.context = context;
        this.userManager = getUserManager(context);
        this.transportType = EzyTransportType.TCP;
    }

    protected abstract EzyUserManager getUserManager(C context);

    @Override
    public EzyResponse encrypted() {
        this.encrypted = true;
        return this;
    }

    @Override
    public EzyResponse encrypted(boolean value) {
        this.encrypted = value;
        return this;
    }

    @Override
    public EzyResponse command(String command) {
        this.command = command;
        return this;
    }

    public EzyResponse params(EzyData params) {
        this.params = params;
        return this;
    }

    @Override
    public EzyResponse user(EzyUser user, boolean exclude) {
        if (user != null) {
            if (exclude) {
                this.exrecipients.addAll(user.getSessions());
            } else {
                this.recipients.addAll(user.getSessions());
            }
        }
        return this;
    }

    @Override
    public EzyResponse users(EzyUser[] users, boolean exclude) {
        for (EzyUser u : users) {
            user(u, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse users(Iterable<EzyUser> users, boolean exclude) {
        for (EzyUser u : users) {
            user(u, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse username(String username, boolean exclude) {
        EzyUser user = userManager.getUser(username);
        return user(user, exclude);
    }

    @Override
    public EzyResponse usernames(String[] usernames, boolean exclude) {
        for (String un : usernames) {
            username(un, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse usernames(Iterable<String> usernames, boolean exclude) {
        for (String un : usernames) {
            username(un, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse session(EzySession session, boolean exclude) {
        if (exclude) {
            this.exrecipients.add(session);
        } else {
            this.recipients.add(session);
        }
        return this;
    }

    @Override
    public EzyResponse sessions(EzySession[] sessions, boolean exclude) {
        for (EzySession s : sessions) {
            session(s, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse sessions(Iterable<EzySession> sessions, boolean exclude) {
        for (EzySession s : sessions) {
            session(s, exclude);
        }
        return this;
    }

    @Override
    public EzyResponse transportType(EzyTransportType transportType) {
        this.transportType = transportType;
        return this;
    }

    @Override
    public void execute() {
        recipients.removeAll(exrecipients);
        EzyData data = newResponseData();
        sendData(data, transportType);
        destroy();
    }

    protected abstract void sendData(EzyData data, EzyTransportType transportType);

    protected final EzyData newResponseData() {
        return newArrayBuilder().append(command).append(params).build();
    }

    @Override
    public void destroy() {
        this.command = null;
        this.params = null;
        this.recipients.clear();
        this.recipients = null;
        this.exrecipients.clear();
        this.exrecipients = null;
        this.context = null;
        this.userManager = null;
    }

}
