package com.tvd12.ezyfoxserver.handler;

import static com.tvd12.ezyfoxserver.constant.EzyCommand.PING;
import static com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction.DISCONNECT_SESSION;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.containsUser;
import static com.tvd12.ezyfoxserver.context.EzyServerContexts.handleException;
import static com.tvd12.ezyfoxserver.exception.EzyRequestHandleException.requestHandleException;

import com.tvd12.ezyfoxserver.command.EzyDisconnectSession;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzySessionError;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySimpleSessionRemovedEvent;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;

public abstract class EzySimpleDataHandler<S extends EzySession> 
        extends EzyUserDataHandler<S> {

    public final void sessionActive() {
        setActive(true);
        getLogger().debug("active session: {}", session);
    }
    
    public void channelInactive() throws Exception {
        if(disconnectReason == null)
            sessionManager.removeSession(session, EzyDisconnectReason.UNKNOWN);
    }
    
    public void channelInactive(EzyConstant reason) throws Exception {
        sessionManager.removeSession(session, reason);
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
            setActive(false);
            processMaxRequestPerSecond();
            return true;
        }
        return false;
    }
    
    protected void processMaxRequestPerSecond() {
        responseError(EzySessionError.MAX_REQUEST_PER_SECOND);
        if(maxRequestPerSecond.getAction() == DISCONNECT_SESSION) {
            if(sessionManager != null) {
                sessionManager.removeSession(session, EzySessionRemoveReason.MAX_REQUEST_PER_SECOND);
            }
        }
    }
    
    protected void handleReceivedData(EzyConstant cmd, EzyArray msg) throws Exception {
        EzyArray data = msg.get(1, EzyArray.class);
        debugLogReceivedData(cmd, data);
        session.addReadRequests(1);
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
        handleRequest(cmd, data);
    }
    
    protected void debugLogReceivedData(EzyConstant cmd, EzyArray data) {
        if(!unloggableCommands.contains(cmd))
            getLogger().debug("command {}, data {}", cmd, data);
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
    
    protected void doHandleRequest(EzyConstant cmd, EzyArray data) throws Exception {
        Object requestParams = mapRequestParams(cmd, data);
        Object request = newRequest(cmd, requestParams);
        interceptRequest(controllers.getInterceptor(cmd), request);
        handleRequest(controllers.getController(cmd), request);
    }
    
    @SuppressWarnings("rawtypes")
    protected void interceptRequest(
            EzyInterceptor interceptor, Object request) throws Exception {
        interceptServerRequest(interceptor, request);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void interceptServerRequest(
            EzyInterceptor interceptor, Object request) throws Exception {
        interceptor.intercept(context, request);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleRequest(EzyController controller, Object request) {
        handleServerRequest(controller, request);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void handleServerRequest(EzyController controller, Object request) {
        controller.handle(context, request);
    }
    
    public void exceptionCaught(Throwable cause) throws Exception {
        getLogger().debug("exception caught at session: " + session, cause);
        EzyExceptionHandler exceptionHandler = exceptionHandlers.get(cause.getClass());
        if(exceptionHandler != null) 
            exceptionHandler.handleException(Thread.currentThread(), cause);
    }

    @Override
    public void onSessionRemoved(EzyConstant reason) {
        notifySessionRemoved(reason);
        setDisconnectReason(reason);
        chechToUnmapUser();
        disconnectSession(reason);
        destroy();
    }
    
    protected void notifySessionRemoved(EzyConstant reason) {
        if(context == null) return;
        EzyEvent event = newSessionRemovedEvent(reason);
        if(user != null)
            notifyAppsSessionRemoved(event);
        notifyPluginsSessionRemoved(event);
    }
    
    protected void notifyAppsSessionRemoved(EzyEvent event) {
        try {
            context.get(EzyFireAppEvent.class)
                .filter(appCtxt -> containsUser(appCtxt, user))
                .fire(EzyEventType.SESSION_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify session removed to apps error", e);
        }
    }
    
    protected void notifyPluginsSessionRemoved(EzyEvent event) {
        try {
            context.get(EzyFirePluginEvent.class)
                .fire(EzyEventType.SESSION_REMOVED, event);
        }
        catch(Exception e) {
            getLogger().error("notify session removed to apps error", e);
        }
    }
    
    protected void disconnectSession(EzyConstant reason) {
        EzyDisconnectSession disconnect = newDisconnectSession(reason);
        disconnect.execute();
    }
    
    protected EzyEvent newSessionRemovedEvent(EzyConstant reason) {
        return EzySimpleSessionRemovedEvent.builder()
                .user(user)
                .session(session)
                .reason(reason)
                .build();
    }
    
}
