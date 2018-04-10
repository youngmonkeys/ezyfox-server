package com.tvd12.ezyfoxserver.controller;

import static com.tvd12.ezyfoxserver.context.EzyServerContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.getSessionManager;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.getSettings;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.getStatistics;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.forEachAppContexts;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.getUserManagementSetting;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.getUserManager;
import static com.tvd12.ezyfoxserver.context.EzyZoneContexts.getZoneId;

import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySimpleUserRemoveDelegate;
import com.tvd12.ezyfoxserver.delegate.EzyUserRemoveDelegate;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
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
import com.tvd12.ezyfoxserver.response.EzyLoginParams;
import com.tvd12.ezyfoxserver.response.EzyLoginResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.statistics.EzyUserStatistics;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public class EzyLoginProcessor 
        extends EzyEntityBuilders 
        implements EzyVoid {

    protected String username;
    protected String password;
    protected EzySession session;
    protected EzyData loginData;
    protected EzyData loginOuputData;
    
    protected final boolean alreadyLoggedIn;
    protected final boolean allowGuestLogin;
    protected final String guestNamePrefix;
    protected final String userNamePattern;

    protected final EzyZone zone;
    protected final EzyZoneSetting zoneSetting;
    protected final EzySettings settings;
    protected final EzyUserStatistics userStats;
    protected final EzyServerContext serverContext;
    protected final EzyZoneContext zoneContext;
    protected final EzyZoneUserManager userManager;
    protected final EzySessionManager<EzySession> sessionManager;
    protected final EzyUserManagementSetting userManagementSetting;
    protected final EzySessionManagementSetting sessionManagementSetting;
    
    protected EzyLoginProcessor(Builder builder) {
        this.username = builder.getUsername();
        this.password = builder.getPassword();
        this.session = builder.getSession();
        this.loginData = builder.getLoginData();
        this.loginOuputData = builder.getLoginOuputData();
        	this.zoneContext = builder.zoneContext;
        	this.zone = zoneContext.getZone();
        	this.zoneSetting = zone.getSetting();
        	this.serverContext = builder.serverContext;
        	this.settings = getSettings(serverContext);
        	this.userManager = getUserManager(zoneContext);
        	this.sessionManager = getSessionManager(serverContext);
        	this.userStats = getStatistics(serverContext).getUserStats();
        	this.userManagementSetting = getUserManagementSetting(zoneContext);
        	this.sessionManagementSetting = settings.getSessionManagement();
        	this.allowGuestLogin = userManagementSetting.isAllowGuestLogin();
        	this.guestNamePrefix = userManagementSetting.getGuestNamePrefix();
        	this.userNamePattern = userManagementSetting.getUserNamePattern();
        this.alreadyLoggedIn = userManager.containsUser(username);
    }
    
    @Override
    public void apply() {
        EzyProcessor.processWithLock(this::process, getLock());
    }
    
    protected void process() {
        checkUsername();
        EzyUser user = getUser();
        checkMaximumSessions(user);
        updateSession(session);
        processReconnect(session);
        mapUserSession(user, session);
        user.addSession(session);
        notifyLoggedIn(user, session);
        fireUserAddedEvent(user);
        response(session, newLoginReponse(user));
        fireSessionLoginEvent(user);
    }
    
    protected void checkUsername() {
        if(!username.matches(userNamePattern))
            throw new EzyLoginErrorException(EzyLoginError.INVALID_USERNAME);
    }
    
    protected void checkMaximumSessions(EzyUser user) {
        if(user.getSessionCount() >= getMaxSession())
            throw new EzyLoginErrorException(EzyLoginError.MAXIMUM_SESSION);
    }
    
    protected void processReconnect(EzySession session) {
        String beforeReconnectToken = session.getBeforeReconnectToken();
        if(beforeReconnectToken == null)
            return;
        EzySession oldsession = sessionManager.getSession(beforeReconnectToken);
        boolean allowReconnect = sessionManagementSetting.isSessionAllowReconnect();
        boolean reconnect = oldsession != null && allowReconnect;
        if(reconnect)
            sessionManager.removeSession(oldsession, EzyDisconnectReason.ANOTHER_SESSION_LOGIN);
    }

    protected void mapUserSession(EzyUser user, EzySession session) {
        if(alreadyLoggedIn) {
            userManager.bind(session, user);
        }
        else {
            userManager.addUser(session, user);
            userStats.addUsers(1);
            userStats.setCurrentUsers(userManager.getUserCount());
        }
    }
    
    protected EzyUser getUser() {
        return alreadyLoggedIn ? userManager.getUser(username) : newUser();
    }
    
    protected void updateSession(EzySession session) {
        session.setLoggedIn(true);
        session.setLoggedInTime(System.currentTimeMillis());
    }
    
    protected void notifyLoggedIn(EzyUser user, EzySession session) {
        EzyHasSessionDelegate hasDelegate = (EzyHasSessionDelegate)session;
        hasDelegate.getDelegate().onSessionLoggedIn(user);
    }
    
    protected void fireSessionLoginEvent(EzyUser user) {
        if(alreadyLoggedIn)
            doFireSessionLoginEvent(newSessionLoginEvent(user));
    }
    
    protected void doFireSessionLoginEvent(EzyEvent event) {
        zoneContext.get(EzyFireAppEvent.class)
            .filter(appCtx -> containsUser(appCtx, username))
            .fire(EzyEventType.USER_SESSION_LOGIN, event);
    }
    
    protected void fireUserAddedEvent(EzyUser user) {
        if(!alreadyLoggedIn)
            doFireUserAddedEvent(newUserAddedEvent(user));
    }
    
    protected void doFireUserAddedEvent(EzyEvent event) {
        try {
            zoneContext.get(EzyFirePluginEvent.class)
                .fire(EzyEventType.USER_ADDED, event);
        }
        catch(Exception e) {
            getLogger().error("user added error", e);
        }
    }
    
    protected EzyUser newUser() {
        EzySimpleUser user = new EzySimpleUser();
        user.setName(getNewUsername(user.getId(), username));
        user.setPassword(password);
        user.setZoneId(zoneSetting.getId());
        user.setMaxIdleTime(userManagementSetting.getUserMaxIdleTime());
        user.setMaxSessions(userManagementSetting.getMaxSessionPerUser());
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
                .context(serverContext)
                .user(user)
                .build();
    }
    
    protected EzyEvent newSessionLoginEvent(EzyUser user) {
        return EzySimpleSessionLoginEvent.builder()
                .session(session)
                .user(user)
                .build();
    }
    
    protected EzyEvent newUserAddedEvent(EzyUser user) {
        return EzySimpleUserAddedEvent.builder()
                .user(user)
                .session(session)
                .loginData(loginData)
                .build();
    }
    
    protected void response(EzySession session, EzyResponse response) {
        serverContext.get(EzySendResponse.class)
            .recipient(session)
            .response(response)
            .execute();
    }
    
    protected EzyResponse newLoginReponse(EzyUser user) {
        EzyLoginParams params = new EzyLoginParams();
        params.setData(loginOuputData);
        params.setUserId(user.getId());
        params.setUsername(user.getName());
        params.setJoinedApps(getJoinedAppsDetails());
        params.setZoneId(getZoneId(zoneContext));
        return new EzyLoginResponse(params);
    }
    
    protected Lock getLock() {
        return userManager.getLock(username);
    }
    
    protected int getMaxSession() {
        return userManagementSetting.getMaxSessionPerUser();
    }
    
    protected EzyArray getJoinedAppsDetails() {
        EzyArrayBuilder builder = newArrayBuilder();
        forEachAppContexts(zoneContext, appCtx -> {
            if(containsUser(appCtx, username))
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
        protected EzyZoneContext zoneContext;
        protected EzyServerContext serverContext;
        
        public Builder event(EzyUserLoginEvent event) {
            this.event = event;
            return this;
        }
        
        public Builder zoneContext(EzyZoneContext zoneContext) {
            this.zoneContext = zoneContext;
            return this;
        }
        
        public Builder serverContext(EzyServerContext serverContext) {
            this.serverContext = serverContext;
            return this;
        }
        
        @Override
        public EzyLoginProcessor build() {
            return new EzyLoginProcessor(this);
        }
        
        protected String getUsername() {
            return event.getUsername();
        }
        
        protected String getPassword() {
            return event.getPassword();
        }
        
        protected EzySession getSession() {
            return event.getSession();
        }
        
        protected EzyData getLoginData() {
            return event.getData();
        }
        
        protected EzyData getLoginOuputData() {
            return event.getOutput();
        }
    }
    
}
