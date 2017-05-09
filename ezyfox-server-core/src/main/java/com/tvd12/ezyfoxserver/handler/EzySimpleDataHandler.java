package com.tvd12.ezyfoxserver.handler;

import static com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction.DISCONNECT_SESSION;
import static com.tvd12.ezyfoxserver.context.EzyContexts.containsUser;

import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFirePluginEvent;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.constant.EzyError;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzySessionRemoveReason;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.impl.EzySessionRemovedEventImpl;
import com.tvd12.ezyfoxserver.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfoxserver.exception.EzyRequestHandleException;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import static com.tvd12.ezyfoxserver.constant.EzyCommand.*;

public abstract class EzySimpleDataHandler<S extends EzySession> 
        extends EzyUserDataHandler<S> {

    public void sessionActive() {
        active = true;
        getLogger().debug("active session: {}, reconnectToken: {}", 
                session.getClientAddress(), session.getReconnectToken());
    }
    
    public void channelInactive() throws Exception {
        if(disconnectReason == null)
            sessionManager.returnSession(session, EzyDisconnectReason.UNKNOWN);
    }
    
    public void channelInactive(EzyDisconnectReason reason) throws Exception {
        sessionManager.returnSession(session, reason);
    }
    
    public void dataReceived(EzyArray msg) throws Exception {
        int cmdId = msg.get(0, int.class);
        EzyConstant cmd = EzyCommand.valueOf(cmdId);
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
        long current = System.currentTimeMillis();
        long offset = current - milestoneRequestTime;
        int count = requestInSecondCount.incrementAndGet();
        int max = maxRequestPerSecond.getValue();
        if(offset > 1000) {
            requestInSecondCount.set(0);
            milestoneRequestTime = current;
        }
        else if(count > max) { 
            setActive(false);
            processMaxRequestPerSecond(count, max);
            return true;
        }
        return false;
    }
    
    protected void processMaxRequestPerSecond(int nrequests, int max) {
        responseError(EzyError.MAX_REQUEST_PER_SECOND);
        if(maxRequestPerSecond.getAction() == DISCONNECT_SESSION) {
            if(sessionManager != null) {
                sessionManager.returnSession(session, EzySessionRemoveReason.MAX_REQUEST_PER_SECOND);
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
        context.get(EzyRunWorker.class).run(() ->
            doHandleRequest(cmd, data)
        );
    }
    
    protected void doHandleRequest(EzyConstant cmd, EzyArray data) {
        try {
            tryHandleRequest(cmd, data);
        }
        catch(Exception e) {
            throw new EzyRequestHandleException(newHandleRequestErrorMessage(cmd, data), e);
        }
    }
    
    protected void tryHandleRequest(EzyConstant cmd, EzyArray data) throws Exception {
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
    
    protected void exceptionCaught(Throwable cause) throws Exception {
        if(cause instanceof EzyMaxRequestSizeException)
            exceptionCaught((EzyMaxRequestSizeException)cause);
    }
    
    private void exceptionCaught(EzyMaxRequestSizeException cause) throws Exception {
        if(sessionManager != null)
            sessionManager.returnSession(session, EzySessionRemoveReason.MAX_REQUEST_SIZE);
    }
    
    @Override
    public void onSessionReturned(EzyConstant reason) {
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
        newDisconnectSession(reason).execute();
    }
    
    protected EzyEvent newSessionRemovedEvent(EzyConstant reason) {
        return EzySessionRemovedEventImpl.builder()
                .user(user)
                .session(session)
                .reason(reason)
                .build();
    }
    
    protected String newHandleRequestErrorMessage(EzyConstant cmd, EzyData data) {
        return "error when handle request command: " + cmd + ", data: " + data;
    }
}
