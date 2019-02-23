package com.tvd12.ezyfoxserver.handler;

import static com.tvd12.ezyfoxserver.constant.EzyCommand.PING;
import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_PER_SECOND;
import static com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction.DISCONNECT_SESSION;
import static com.tvd12.ezyfoxserver.exception.EzyRequestHandleException.requestHandleException;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzySessionError;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleSessionRemovedEvent;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;
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
    
    public void streamingReceived(byte[] bytes) throws Exception {
        if(!validateState()) return;
        if(!validateSession()) return;
        handleReceivedStreaming(bytes);
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
    
    protected void handleReceivedStreaming(byte[] bytes) throws Exception {
        updateSessionBeforeHandleRequest();
        handleReceivedStreaming0(bytes);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleReceivedStreaming0(byte[] bytes) throws Exception {
        EzyStreamingRequest request = newStreamingRequest(bytes);
        try {
            EzyInterceptor interceptor = controllers.getStreamingInterceptor();
            interceptor.intercept(context, request);
            EzyStreamingController controller = controllers.getStreamingController();
            controller.handle(zoneContext, request);
        }
        catch(Exception e) {
            context.handleException(Thread.currentThread(), e);
        }
        finally {
            request.release();
        }
    }
    
    protected void handleReceivedData(EzyConstant cmd, EzyArray msg) throws Exception {
        EzyArray data = msg.get(1, EzyArray.class);
        debugLogReceivedData(cmd, data);
        updateSessionBeforeHandleRequest();
        handleRequest(cmd, data);
    }
    
    private void updateSessionBeforeHandleRequest() {
        session.addReadRequests(1);
        session.setLastReadTime(System.currentTimeMillis());
        session.setLastActivityTime(System.currentTimeMillis());
    }
    
    protected void debugLogReceivedData(EzyConstant cmd, EzyArray data) {
        if(!unloggableCommands.contains(cmd))
            logger.debug("received from: {}, command: {}, data: {}", session.getName(), cmd, data);
    }
    
    @SuppressWarnings("rawtypes")
    protected void handleRequest(EzyConstant cmd, EzyArray data) {
        EzyRequest request = newRequest(cmd, data);
        try {
            EzyInterceptor interceptor = controllers.getInterceptor(cmd);
            interceptor.intercept(context, request);
            EzyController controller = controllers.getController(cmd);
            controller.handle(context, request);
        }
        catch(Exception e) {
            if(context != null) {
                Throwable throwable = requestHandleException(cmd, data, e);
                context.handleException(Thread.currentThread(), throwable);
            }
            else {
                if(active) {
                    logger.warn("fatal error, please add an issue to ezyfox-server github with log: " + toString() + "\nand stacktrace: ", e);
                }
                else {
                    logger.warn("can't handle command: " + cmd + " and data: " + data + ", this session maybe destroyed (session = " + session + "), error message: " + e.getMessage());
                }
            }
            
        }
        finally {
            request.release();
        }
    }
    
    public void exceptionCaught(Throwable cause) throws Exception {
        logger.debug("exception caught at session: " + session, cause);
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
            zoneContext.broadcastApps(EzyEventType.SESSION_REMOVED, event, user, true);
        }
        catch(Exception e) {
            logger.error("notify session: " + session + " removed to apps error", e);
        }
    }
    
    protected void notifyPluginsSessionRemoved(EzyEvent event) {
        try {
            zoneContext.broadcastPlugins(EzyEventType.SESSION_REMOVED, event, true);
        }
        catch(Exception e) {
            logger.error("notify session: " + session + " removed to apps error", e);
        }
    }
    
    protected void closeSession(EzyConstant reason) {
        try {
            closeSession.close(session, reason);
        }
        catch(Exception ex) {
            logger.error("close session: " + session + " with reason: " + reason + " error", ex);
        }
    }
    
    protected EzyEvent newSessionRemovedEvent(EzyConstant reason) {
        return new EzySimpleSessionRemovedEvent(user, session, reason);
    }
    
}
