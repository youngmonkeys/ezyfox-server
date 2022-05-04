package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyController;
import com.tvd12.ezyfoxserver.controller.EzyStreamingController;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleSessionRemovedEvent;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.request.EzyRequest;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;

import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_PER_SECOND;
import static com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction.DISCONNECT_SESSION;
import static com.tvd12.ezyfoxserver.exception.EzyRequestHandleException.requestHandleException;

public abstract class EzySimpleDataHandler<S extends EzySession>
    extends EzyUserDataHandler<S> {

    public EzySimpleDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
    }

    public void dataReceived(EzyCommand cmd, EzyArray msg) {
        if (!active) {
            return;
        }
        if (isInactivatedSession()) {
            return;
        }
        handleReceivedData(cmd, msg);
    }

    public void streamingReceived(byte[] bytes) {
        if (!active) {
            return;
        }
        if (isInactivatedSession()) {
            return;
        }
        handleReceivedStreaming(bytes);
    }

    protected boolean isInactivatedSession() {
        return session == null || !session.isActivated();
    }

    @SuppressWarnings("unchecked")
    public void processMaxRequestPerSecond() {
        responseError();
        if (maxRequestPerSecond.getAction() == DISCONNECT_SESSION) {
            this.active = false;
            if (sessionManager != null) {
                sessionManager.removeSession(session, MAX_REQUEST_PER_SECOND);
            }
        }
    }

    protected void handleReceivedStreaming(byte[] bytes) {
        updateSessionBeforeHandleRequest();
        doHandleReceivedStreaming(bytes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void doHandleReceivedStreaming(byte[] bytes) {
        try {
            EzyStreamingRequest request = newStreamingRequest(bytes);
            try {
                EzyInterceptor interceptor = controllers.getStreamingInterceptor();
                interceptor.intercept(context, request);
                EzyStreamingController controller = controllers.getStreamingController();
                controller.handle(zoneContext, request);
            } finally {
                request.release();
            }
        } catch (Throwable e) {
            context.handleException(Thread.currentThread(), e);
        }
    }

    protected void handleReceivedData(EzyConstant cmd, EzyArray msg) {
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
        boolean debug = settings.isDebug();
        if (debug && !ignoredLogCommands.contains(cmd)) {
            logger.debug("received from: {}, command: {}, data: {}", session.getName(), cmd, data);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void handleRequest(EzyConstant cmd, EzyArray data) {
        try {
            EzyRequest request = newRequest(cmd, data);
            try {
                EzyInterceptor interceptor = controllers.getInterceptor(cmd);
                interceptor.intercept(context, request);
                EzyController controller = controllers.getController(cmd);
                controller.handle(context, request);
            } finally {
                request.release();
            }
        } catch (Exception e) {
            if (context != null) {
                Throwable throwable = requestHandleException(session, cmd, data, e);
                context.handleException(Thread.currentThread(), throwable);
            } else {
                if (active) {
                    logger.warn(
                        "fatal error, please add an issue to ezyfox-server github " +
                            "with log: {}\nand stacktrace: ",
                        this,
                        e
                    );
                } else {
                    logger.warn("can't handle command: {} and data: {}, this session " +
                        "maybe destroyed (session: {}), error message: {}",
                        cmd,
                        data,
                        session,
                        e.getMessage()
                    );
                }
            }
        }
    }

    public void exceptionCaught(Throwable cause) {
        logger.debug("exception caught at session: {}", session, cause);
        EzyExceptionHandler exceptionHandler = exceptionHandlers.get(cause.getClass());
        if (exceptionHandler != null) {
            exceptionHandler.handleException(Thread.currentThread(), cause);
        }
    }

    public void channelInactive(EzyConstant reason) {
        lock.lock();
        try {
            if (destroyed) {
                return;
            }
            destroyed = true;
        } finally {
            lock.unlock();
        }
        doChannelInactive(reason);
    }

    protected void doChannelInactive(EzyConstant reason) {
        removeSession();
        notifySessionRemoved(reason);
        closeSession(reason);
        checkToUnmapUser(reason);
        destroy();
    }

    @SuppressWarnings("unchecked")
    protected void removeSession() {
        if (sessionManager != null) {
            sessionManager.clearSession(session);
        }
    }

    protected void notifySessionRemoved(EzyConstant reason) {
        if (zoneContext != null) {
            EzyEvent event = newSessionRemovedEvent(reason);
            notifyAppsSessionRemoved(event);
            notifyPluginsSessionRemoved(event);
        }
    }

    protected void notifyAppsSessionRemoved(EzyEvent event) {
        if (user != null) {
            doNotifyAppsSessionRemoved(event);
        }
    }

    protected void doNotifyAppsSessionRemoved(EzyEvent event) {
        try {
            zoneContext.broadcastApps(EzyEventType.SESSION_REMOVED, event, user, true);
        } catch (Exception e) {
            logger.error("notify session: {} removed to apps error", session, e);
        }
    }

    protected void notifyPluginsSessionRemoved(EzyEvent event) {
        try {
            zoneContext.broadcastPlugins(EzyEventType.SESSION_REMOVED, event, true);
        } catch (Exception e) {
            logger.error("notify session: {} removed to apps error", session, e);
        }
    }

    protected void closeSession(EzyConstant reason) {
        try {
            closeSession.close(session, reason);
        } catch (Exception ex) {
            logger.error("close session: {} with reason: {} error", session, reason, ex);
        }
    }

    protected EzyEvent newSessionRemovedEvent(EzyConstant reason) {
        return new EzySimpleSessionRemovedEvent(user, session, reason);
    }
}
