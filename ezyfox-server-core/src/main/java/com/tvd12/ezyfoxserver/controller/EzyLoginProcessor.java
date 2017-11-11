package com.tvd12.ezyfoxserver.controller;

import static com.tvd12.ezyfoxserver.context.EzyContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyContexts.forEachAppContexts;
import static com.tvd12.ezyfoxserver.context.EzyContexts.getSettings;
import static com.tvd12.ezyfoxserver.context.EzyContexts.getUserManager;

import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserRemoveDelegate;
import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyHasSessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleSessionLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleUserAddedEvent;
import com.tvd12.ezyfoxserver.exception.EzyLoginErrorException;
import com.tvd12.ezyfoxserver.function.EzyVoid;
import com.tvd12.ezyfoxserver.response.EzyLoginResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;

public class EzyLoginProcessor 
        extends EzyEntityBuilders 
        implements EzyVoid {

    protected boolean alreadyLoggedIn;
    protected boolean allowGuestLogin;
    protected String guestNamePrefix;
    protected String userNamePattern;

    protected EzySettings settings;
    protected EzyUserLoginEvent event;
    protected EzyServerContext context;
    protected EzyServerUserManager userManager;
    protected EzyUserManagementSetting userManagementSetting;
    
    protected EzyLoginProcessor(Builder builder) {
    	this.event = builder.event;
    	this.context = builder.context;
    	this.settings = getSettings(context);
    	this.userManager = getUserManager(context);
    	this.userManagementSetting = settings.getUserManagement();
    	this.allowGuestLogin = userManagementSetting.isAllowGuestLogin();
    	this.guestNamePrefix = userManagementSetting.getGuestNamePrefix();
    	this.userNamePattern = userManagementSetting.getUserNamePattern();
        this.alreadyLoggedIn = userManager.containsUser(event.getUsername());
    }
    
    @Override
    public void apply() {
        EzyProcessor.processWithLock(this::process, getLock());
    }
    
    protected void process() {
        checkUsername();
        EzyUser user = getUser();
        checkMaximumSessions(user);
        EzySession session = getSession();
        updateSession(session);
        mapUserSession(user, session);
        user.addSession(session);
        notifyLoggedIn(user, session);
        fireUserAddedEvent(user);
        response(user, newLoginReponse(user));
        fireSessionLoginEvent(user);
    }
    
    protected void checkUsername() {
        if(!getUsername().matches(userNamePattern))
            throw new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
    }
    
    protected void checkMaximumSessions(EzyUser user) {
        if(user.getSessionCount() >= getMaxSession())
            throw new EzyLoginErrorException(EzyLoginError.MAXIMUM_SESSION);
    }

    protected void mapUserSession(EzyUser user, EzySession session) {
        if(alreadyLoggedIn)
            userManager.bind(session, user);
        else
            userManager.addUser(session, user);
    }
    
    protected EzyUser getUser() {
        return alreadyLoggedIn ? userManager.getUser(getUsername()) : newUser();
    }
    
    protected void updateSession(EzySession session) {
        session.setLoggedIn(true);
        session.setLoggedInTime(System.currentTimeMillis());
    }
    
    protected void notifyLoggedIn(EzyUser user, EzySession session) {
        EzyHasSessionDelegate hasDelegate = (EzyHasSessionDelegate)getSession();
        hasDelegate.getDelegate().onSessionLoggedIn(user);
    }
    
    protected void fireSessionLoginEvent(EzyUser user) {
        if(alreadyLoggedIn)
            doFireSessionLoginEvent(newSessionLoginEvent(user));
    }
    
    protected void doFireSessionLoginEvent(EzyEvent event) {
        context.get(EzyFireAppEvent.class)
            .filter(appCtx -> containsUser(appCtx, getUsername()))
            .fire(EzyEventType.USER_SESSION_LOGIN, event);
    }
    
    protected void fireUserAddedEvent(EzyUser user) {
        if(!alreadyLoggedIn)
            doFireUserAddedEvent(newUserAddedEvent(user));
    }
    
    protected void doFireUserAddedEvent(EzyEvent event) {
        try {
            context.get(EzyFirePluginEvent.class)
                .fire(EzyEventType.USER_ADDED, event);
        }
        catch(Exception e) {
            getLogger().error("user added error", e);
        }
    }
    
    protected EzyUser newUser() {
        EzySettings settings = getSettings(context);
        EzyUserManagementSetting ust = settings.getUserManagement();
        EzySimpleUser user = new EzySimpleUser();
        user.setName(getNewUsername(user.getId(), event.getUsername()));
        user.setPassword(event.getPassword());
        user.setMaxIdleTime(ust.getUserMaxIdleTime());
        user.setMaxSessions(ust.getMaxSessionPerUser());
        user.setRemoveDelegate(newUserRemoveDelegate(user));
        return user;
    }
    
    protected String getNewUsername(long userId, String currentName) {
        if(!StringUtils.isEmpty(currentName))
            return currentName;
        if(allowGuestLogin)
            return guestNamePrefix + userId;
        throw new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
    }
    
    protected EzyUserRemoveDelegate newUserRemoveDelegate(EzyUser user) {
        return EzySimpleUserRemoveDelegate.builder()
                .context(context)
                .user(user)
                .build();
    }
    
    protected EzyEvent newSessionLoginEvent(EzyUser user) {
        return EzySimpleSessionLoginEvent.builder()
                .session(getSession())
                .user(user)
                .build();
    }
    
    protected EzyEvent newUserAddedEvent(EzyUser user) {
        return EzySimpleUserAddedEvent.builder()
                .user(user)
                .session(getSession())
                .loginData(getLoginData())
                .build();
    }
    
    protected void response(EzyUser user, EzyResponse response) {
        context.get(EzySendResponse.class)
            .sender(user)
            .response(response)
            .execute();
    }
    
    protected EzyResponse newLoginReponse(EzyUser user) {
        return EzyLoginResponse.builder()
                .data(event.getOutput())
                .userId(user.getId())
                .username(user.getName())
                .joinedApps(getJoinedAppsInfo())
                .build();
                
    }
    
    protected Lock getLock() {
        return userManager.getLock(getUsername());
    }
    
    protected String getUsername() {
        return event.getUsername();
    }
    
    protected EzySession getSession() {
        return event.getSession();
    }
    
    protected EzyArray getLoginData() {
        return event.getData();
    }
    
    protected int getMaxSession() {
        return context
                .getServer()
                .getSettings()
                .getUserManagement()
                .getMaxSessionPerUser();
    }
    
    protected EzyArray getJoinedAppsInfo() {
        EzyArrayBuilder builder = newArrayBuilder();
        forEachAppContexts(context, (appCtx) -> {
            if(containsUser(appCtx, getUsername()))
                builder.append(newJoinedAppInfo(appCtx));
        });
        return builder.build();
    }
    
    @SuppressWarnings("unchecked")
    protected EzyArrayBuilder newJoinedAppInfo(EzyAppContext appCtx) {
        EzyAppSetting app = appCtx.getApp().getSetting();
        return newArrayBuilder().append(app.getId(), app.getName());
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyLoginProcessor> {
        protected EzyUserLoginEvent event;
        protected EzyServerContext context;
        
        public Builder event(EzyUserLoginEvent event) {
            this.event = event;
            return this;
        }
        
        public Builder context(EzyServerContext context) {
            this.context = context;
            return this;
        }
        
        @Override
        public EzyLoginProcessor build() {
            return new EzyLoginProcessor(this);
        }
    }
    
}
