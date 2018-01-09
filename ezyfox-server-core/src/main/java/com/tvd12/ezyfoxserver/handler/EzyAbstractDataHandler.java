package com.tvd12.ezyfoxserver.handler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzySendResponse;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyIError;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.response.EzyErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting.EzyMaxRequestPerSecond;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyRequestFrame;
import com.tvd12.ezyfoxserver.statistics.EzyRequestFrameSecond;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public class EzyAbstractDataHandler<S extends EzySession> 
        extends EzyLoggable
        implements EzyDestroyable {

    protected S session;
    protected EzyUser user;
    protected EzyServerContext context;
    protected EzyServerControllers controllers;
    protected EzyServerUserManager userManager;
    protected EzySessionManager<S> sessionManager;
    protected Lock lock = new ReentrantLock();
    
    protected EzyConstant disconnectReason;
    
    protected EzySettings settings;
    protected Set<EzyConstant> unloggableCommands;
    protected EzySessionManagementSetting sessionManagementSetting;
    
    //===== for measure max request per second =====
    protected EzyRequestFrame requestFrameInSecond;
    protected EzyMaxRequestPerSecond maxRequestPerSecond;
    //=====  =====
    
    protected volatile boolean active = false;
    protected Map<Class<?>, EzyExceptionHandler> exceptionHandlers = newExceptionHandlers();
    
    protected EzyAppContext getAppContext(int appId) {
        return context.getAppContext(appId);
    }
    
    public void setContext(EzyServerContext ctx) {
        this.context = ctx;
        this.userManager = getUserManager();
        this.sessionManager = getSessionManager();
        this.controllers = getServer().getControllers();
        
        this.settings = getServer().getSettings();
        this.sessionManagementSetting = settings.getSessionManagement();
        this.unloggableCommands = settings.getLogger().getIgnoredCommands().getCommands();
        this.maxRequestPerSecond = sessionManagementSetting.getSessionMaxRequestPerSecond();
        this.requestFrameInSecond = new EzyRequestFrameSecond(maxRequestPerSecond.getValue());
    }
    
    protected void setActive(boolean value) {
        this.active = value;
    }
    
    protected void setDisconnectReason(EzyConstant reason) {
        this.disconnectReason = reason;
    }
    
    protected EzyServer getServer() {
        return context.getServer();
    }
    
    protected EzyManagers getManagers() {
        return getServer().getManagers();
    }

    @SuppressWarnings("unchecked")
    protected EzySessionManager<S> getSessionManager() {
        return getManagers().getManager(EzySessionManager.class);
    }
    
    protected EzyServerUserManager getUserManager() {
        return getManagers().getManager(EzyServerUserManager.class);
    }
    
    protected void response(EzyResponse response) {
        if(context != null)
            context.get(EzySendResponse.class).sender(session).response(response).execute();
    }
    
    protected void responseError(EzyIError error) {
        response(EzyErrorResponse.builder().error(error).build());
    }
    
    private Map<Class<?>, EzyExceptionHandler> newExceptionHandlers() {
        Map<Class<?>, EzyExceptionHandler> handlers = new ConcurrentHashMap<>();
        addExceptionHandlers(handlers);
        return handlers;
    }
    
    protected void addExceptionHandlers(Map<Class<?>, EzyExceptionHandler> handlers) {
    }
    
    @Override
    public void destroy() {
        this.user = null;
        this.session = null;
        this.context = null;
        this.controllers = null;
        this.userManager = null;
        this.sessionManager = null;
    }
    
}
