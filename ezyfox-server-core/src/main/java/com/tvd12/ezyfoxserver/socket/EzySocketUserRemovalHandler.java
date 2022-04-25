package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyApplication;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRemovedEvent;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

import java.util.Collection;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public class EzySocketUserRemovalHandler extends EzySocketAbstractEventHandler {

    protected final EzySocketUserRemovalQueue userRemovalQueue;

    public EzySocketUserRemovalHandler(EzySocketUserRemovalQueue userRemovalQueue) {
        this.userRemovalQueue = userRemovalQueue;
    }

    @Override
    public void handleEvent() {
        processUserRemovalQueue();
    }

    @Override
    public void destroy() {
        processWithLogException(userRemovalQueue::clear);
    }

    private void processUserRemovalQueue() {
        try {
            EzySocketUserRemoval removal = userRemovalQueue.take();
            processUserRemoval(removal);
        } catch (InterruptedException e) {
            logger.info("user-removal-handler thread interrupted: {}", Thread.currentThread());
        } catch (Throwable throwable) {
            logger.warn("problems in user-removal-handler, thread: {}", Thread.currentThread(), throwable);
        }
    }

    private void processUserRemoval(EzySocketUserRemoval removal) {
        try {
            processUserRemoval0(removal);
        } finally {
            removal.release();
        }
    }

    private void processUserRemoval0(EzySocketUserRemoval removal) {
        EzyUser user = removal.getUser();
        try {
            EzyConstant reason = removal.getReason();
            EzyZoneContext zoneContext = removal.getZoneContext();
            EzyUserRemovedEvent event = newUserRemovedEvent(user, reason);
            removeUserFromApps(zoneContext, event);
            notifyUserRemovedToPlugins(zoneContext, event);
        } finally {
            user.destroy();
        }
        logger.debug("user {} has destroyed", user);
    }

    protected void notifyUserRemovedToPlugins(EzyZoneContext context, EzyUserRemovedEvent event) {
        try {
            context.broadcastPlugins(EzyEventType.USER_REMOVED, event, true);
        } catch (Exception e) {
            String zoneName = context.getZone().getSetting().getName();
            logger.error("zone: {}, notify to plugins user: {} removed error", zoneName, event.getUser(), e);
        }
    }

    protected void removeUserFromApps(EzyZoneContext zoneContext, EzyUserRemovedEvent event) {
        Collection<EzyAppContext> appContexts = zoneContext.getAppContexts();
        for (EzyAppContext appContext : appContexts) {
            removeUserFromApp(appContext, event);
        }
    }

    protected void removeUserFromApp(EzyAppContext appContext, EzyUserRemovedEvent event) {
        EzyUser user = event.getUser();
        EzyApplication app = appContext.getApp();
        EzyAppUserManager userManager = app.getUserManager();
        try {
            boolean contains = userManager.containsUser(user);
            if (contains) {
                userManager.removeUser(user, event.getReason());
            }
        } catch (Exception e) {
            String appName = app.getSetting().getName();
            logger.error("remove user: {} from app: {} error", event.getUser(), appName, e);
        }
    }

    protected EzyUserRemovedEvent newUserRemovedEvent(EzyUser user, EzyConstant reason) {
        return new EzySimpleUserRemovedEvent(user, reason);
    }
}
