package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.exception.EzyMaxRequestSizeException;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyExceptionHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyCloseSession;
import com.tvd12.ezyfoxserver.constant.EzySessionError;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.response.EzyErrorParams;
import com.tvd12.ezyfoxserver.response.EzyErrorResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting.EzyMaxRequestPerSecond;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tvd12.ezyfoxserver.constant.EzyDisconnectReason.MAX_REQUEST_SIZE;

public abstract class EzyAbstractDataHandler<S extends EzySession>
    extends EzyLoggable
    implements EzySessionDelegate, EzyDestroyable {

    protected final Lock lock = new ReentrantLock();
    protected S session;
    protected EzyChannel channel;
    protected EzyUser user;
    protected EzyServer server;
    protected EzyServerContext context;
    protected EzyZoneContext zoneContext;
    protected EzyCloseSession closeSession;
    protected EzyServerControllers controllers;
    protected EzyZoneUserManager userManager;
    @SuppressWarnings("rawtypes")
    protected EzySessionManager sessionManager;
    protected EzySettings settings;
    protected Set<EzyConstant> ignoredLogCommands;
    protected EzySessionManagementSetting sessionManagementSetting;
    protected EzyMaxRequestPerSecond maxRequestPerSecond;
    protected volatile boolean active = true;
    protected volatile boolean destroyed = false;
    protected Map<Class<?>, EzyExceptionHandler> exceptionHandlers
        = newExceptionHandlers();

    public EzyAbstractDataHandler(EzyServerContext ctx, S session) {
        this.context = ctx;
        this.session = session;
        this.channel = session.getChannel();
        this.server = context.getServer();
        this.controllers = server.getControllers();
        this.sessionManager = server.getSessionManager();
        this.closeSession = context.get(EzyCloseSession.class);

        this.settings = server.getSettings();
        this.sessionManagementSetting = settings.getSessionManagement();
        this.maxRequestPerSecond = sessionManagementSetting.getSessionMaxRequestPerSecond();
        this.ignoredLogCommands = settings.getLogger().getIgnoredCommands().getCommands();
        ((EzyAbstractSession) this.session).setDelegate(this);
    }

    protected EzyZoneUserManager getUserManager(int zoneId) {
        EzyZoneContext zoneContext = context.getZoneContext(zoneId);
        return zoneContext.getZone().getUserManager();
    }

    protected void response(EzyResponse response) {
        if (context != null) {
            context.send(response, session, false);
        }
    }

    protected void responseError() {
        EzyErrorParams params = new EzyErrorParams();
        params.setError(EzySessionError.MAX_REQUEST_PER_SECOND);
        response(new EzyErrorResponse(params));
    }

    @SuppressWarnings("unchecked")
    private Map<Class<?>, EzyExceptionHandler> newExceptionHandlers() {
        Map<Class<?>, EzyExceptionHandler> handlers = new ConcurrentHashMap<>();
        handlers.put(EzyMaxRequestSizeException.class, (thread, throwable) -> {
            if (sessionManager != null) {
                sessionManager.removeSession(session, MAX_REQUEST_SIZE);
            }
        });
        return handlers;
    }

    @Override
    public void destroy() {
        this.active = false;
        this.destroyed = true;
        if (session != null) {
            session.destroy();
        }
        this.session = null;
        this.channel = null;
        this.server = null;
        this.user = null;
        this.context = null;
        this.zoneContext = null;
        this.controllers = null;
        this.userManager = null;
        this.closeSession = null;
        this.sessionManager = null;
        this.settings = null;
        this.ignoredLogCommands = null;
        this.sessionManagementSetting = null;
        if (exceptionHandlers != null) {
            this.exceptionHandlers.clear();
        }
        this.exceptionHandlers = null;
    }

    @Override
    public String toString() {
        return "(" +
            "\n\tactive: " + active +
            "\n\tsession: " + session +
            "\n\tchannel: " + channel +
            "\n\tserver: " + server +
            "\n\tuser: " + user +
            "\n\tcontext: " + context +
            "\n\tzoneContext: " + zoneContext +
            "\n\tcontrollers: " + controllers +
            "\n\tuserManager: " + userManager +
            "\n\tcloseSession: " + closeSession +
            "\n\tsessionManager: " + sessionManager +
            "\n\tlock: " + lock +
            "\n\tsettings: " + settings +
            "\n\tignoredLogCommands: " + ignoredLogCommands +
            "\n\tsessionManagementSetting: " + sessionManagementSetting +
            "\n\texceptionHandlers: " + exceptionHandlers +
            "\n)";
    }
}
