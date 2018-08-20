package com.tvd12.ezyfoxserver.handler;

import static com.tvd12.ezyfoxserver.constant.EzyCommand.PING;
import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_PER_SECOND;
import static com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction.DISCONNECT_SESSION;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.handleException;
import static com.tvd12.ezyfoxserver.exception.EzyRequestHandleException.requestHandleException;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzySessionError;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleSessionRemovedEvent;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.request.EzyRequest;

@SuppressWarnings("unchecked")
public abstract class EzySimpleDataHandler<S extends EzySession> 
        extends EzyUserDataHandler<S> {

    public EzySimpleDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
    }

    public void dataReceived(EzyCommand cmd, EzyArray msg) throws Exception {
        if(!validateState()) return;
        if(!validateSession()) return;
        if(cmd != PING && checkMaxRequestPerSecond()) return;
        handleReceivedData(cmd, msg);
    }
    
    protected boolean validateState() {
        return active;
    }
    
    protected boolean validateSession() {
        return session != null && session.isActivated();
    }
    
    protected boolean checkMaxRequestPerSecond() {
        if(requestFrameInSecond.isExpired())
            requestFrameInSecond = requestFrameInSecond.nextFrame();
        boolean hasMaxRequest = requestFrameInSecond.addRequests(1);
        if(hasMaxRequest) { 
            processMaxRequestPerSecond();
            return true;
        }
        return false;
    }
    
    protected void processMaxRequestPerSecond() {
        responseError(EzySessionError.MAX_REQUEST_PER_SECOND);
        if(maxRequestPerSecond.getAction() == DISCONNECT_SESSION) {
            this.active = false;
            if(sessionManager != null)
                sessionManager.removeSession(session, MAX_REQUEST_PER_SECOND);
        }
    }
    
    protected void handleReceivedData(EzyConstant cmd, EzyArray msg) throws Exception {
        EzyArray data = msg.get(1, EzyArray.class);
        debugLogReceivedData(cmd, data);
        session.addReadRequests(1);
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
        updateUserByReceivedData();
        handleRequest(cmd, data);
    }
    
    protected void debugLogReceivedData(EzyConstant cmd, EzyArray data) {
        if(!unloggableCommands.contains(cmd))
            getLogger().debug("received from: {}, command: {}, data: {}", session.getName(), cmd, data);
    }
    
    public void updateUserByReceivedData() {
        if(user != null) {
            user.setStartIdleTime(System.currentTimeMillis());
        }
    }
    
    protected void handleRequest(EzyConstant cmd, EzyArray data) {
        try {
            doHandleRequest(cmd, data);
        }
        catch(Exception e) {
            Throwable throwable = requestHandleException(cmd, data, e);
            handleException(context, Thread.currentThread(), throwable);
        }
    }
    
    @SuppressWarnings("rawtypes")
    protected void doHandleRequest(EzyConstant cmd, EzyArray data) throws Exception {
        EzyRequest request = newRequest(cmd, data);
        try {
            EzyInterceptor interceptor = controllers.getInterceptor(cmd);
            interceptRequest(interceptor, request);
            EzyController controller = controllers.getController(cmd);
            handleRequest(controller, request);
        }
        finally {
            request.release();
        }
    }
    
    @SuppressWarnings("rawtypes")
    protected void interceptRequest(
            EzyInterceptor interceptor, Object request) throws Exception {
        interceptServerRequest(interceptor, request);
    }
    
    @SuppressWarnings({ "rawtypes" })
    protected void interceptServerRequest(
            EzyInterceptor interceptor, Object request) throws Exception {
        interceptor.intercept(context, request);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleRequest(EzyController controller, Object request) {
        handleServerRequest(controller, request);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleServerRequest(EzyController controller, Object request) {
        controller.handle(context, request);
    }
    
    public void exceptionCaught(Throwable cause) throws Exception {
        getLogger().debug("exception caught at session: " + session, cause);
        EzyExceptionHandler exceptionHandler = exceptionHandlers.get(cause.getClass());
        if(exceptionHandler != null) 
            exceptionHandler.handleException(Thread.currentThread(), cause);
    }

    public void channelInactive(EzyConstant reason) {
        removeSession();
        notifySessionRemoved(reason);
        closeSession(reason);
        checkToUnmapUser(reason);
        destroy();
    }
    
    protected void removeSession() {
        sessionManager.clearSession(session);
    }
    
    protected void notifySessionRemoved(EzyConstant reason) {
        if(zoneContext != null) { 
            EzyEvent event = newSessionRemovedEvent(reason);
            notifyAppsSessionRemoved(event);
            notifyPluginsSessionRemoved(event);
        }
    }
    
    protected void notifyAppsSessionRemoved(EzyEvent event) {
        if(user != null)
            notifyAppsSessionRemoved0(event);
    }
    
    protected void notifyAppsSessionRemoved0(EzyEvent event) {
        try {
            zoneContext.get(EzyFireAppEvent.class)
                .filter(appCtxt -> containsUser(appCtxt, user))
                .fire(EzyEventType.SESSION_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify session: " + session + " removed to apps error", e);
        }
    }
    
    protected void notifyPluginsSessionRemoved(EzyEvent event) {
        try {
            zoneContext.get(EzyFirePluginEvent.class)
                .fire(EzyEventType.SESSION_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify session: " + session + " removed to apps error", e);
        }
    }
    
    protected void closeSession(EzyConstant reason) {
        try {
            EzyCloseSession close = newCloseSession(reason);
            close.execute();
        }
        catch(Exception ex) {
            getLogger().error("close session: " + session + " with reason: " + reason + " error", ex);
        }
    }
    
    protected EzyEvent newSessionRemovedEvent(EzyConstant reason) {
        return new EzySimpleSessionRemovedEvent(user, session, reason);
    }
    
}
