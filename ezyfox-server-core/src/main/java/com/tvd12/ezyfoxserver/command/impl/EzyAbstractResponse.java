package com.tvd12.ezyfoxserver.command.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.controller.EzyMessageController;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;

public abstract class EzyAbstractResponse<C extends EzyContext> 
        extends EzyMessageController 
        implements EzyResponse {

    protected String command;
    protected EzyData params;
    protected Set<EzySession> recipients = new HashSet<>();
    protected Set<EzySession> exrecipients = new HashSet<>();
    
    protected final C context;
    protected final EzyUserManager userManager;
    
    public EzyAbstractResponse(C context) {
        this.context = context;
        this.userManager = getUserManager(context);
    }
    
    protected abstract EzyUserManager getUserManager(C context);
    
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
        if(user != null) {
            if(exclude)
                this.exrecipients.addAll(user.getSessions());
            else
                this.recipients.addAll(user.getSessions());
        }
        return this;
    }
    
    @Override
    public EzyResponse users(EzyUser[] users, boolean exclude) {
        return users(Arrays.asList(users), exclude);
    }

    @Override
    public EzyResponse users(Iterable<EzyUser> users, boolean exclude) {
        users.forEach(u -> user(u, exclude));
        return this;
    }
    
    @Override
    public EzyResponse username(String username, boolean exclude) {
        return user(userManager.getUser(username), exclude);
    }
    
    @Override
    public EzyResponse usernames(String[] usernames, boolean exclude) {
        Arrays.stream(usernames).forEach(un -> username(un, exclude));
        return this;
    }
    
    @Override
    public EzyResponse usernames(Iterable<String> usernames, boolean exclude) {
        usernames.forEach(un -> username(un, exclude));
        return this;
    }

    @Override
    public EzyResponse session(EzySession session, boolean exclude) {
        if(exclude)
            this.exrecipients.add(session);
        else
            this.recipients.add(session);
        return this;
    }
    
    @Override
    public EzyResponse sessions(EzySession[] sessions, boolean exclude) {
        Arrays.stream(sessions).forEach(s -> session(s, exclude));
        return this;
    }
    
    @Override
    public EzyResponse sessions(Iterable<EzySession> sessions, boolean exclude) {
        sessions.forEach(s -> session(s, exclude));
        return this;
    }
    
    @Override
    public Boolean execute() {
        com.tvd12.ezyfoxserver.response.EzyResponse response = newResponse();
        recipients.removeAll(exrecipients);
        recipients.forEach(s -> response(s, response));
        return Boolean.TRUE;
    }
    
    protected void response(EzySession session, 
            com.tvd12.ezyfoxserver.response.EzyResponse response) {
        response(context, session, response);
    }
    
    protected com.tvd12.ezyfoxserver.response.EzyResponse newResponse() {
        return newResponseBuilder(newData()).build();
    }

    protected abstract com.tvd12.ezyfoxserver.response.EzyResponse.Builder 
            newResponseBuilder(EzyData data);

    protected EzyData newData() {
        return newArrayBuilder()
                .append(command)
                .append(params)
                .build();
    }

}
