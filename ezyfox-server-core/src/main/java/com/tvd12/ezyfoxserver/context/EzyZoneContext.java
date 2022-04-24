package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.EzyZone;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import java.util.function.Predicate;

public interface EzyZoneContext extends EzyComplexContext {

    EzyZone getZone();

    EzyServerContext getParent();

    EzyAppContext getAppContext(String appName);

    EzyPluginContext getPluginContext(String pluginName);

    void broadcastPlugins(
        EzyConstant type,
        EzyEvent event,
        boolean catchException
    );

    void broadcastApps(
        EzyConstant type,
        EzyEvent event,
        boolean catchException
    );

    void broadcastApps(
        EzyConstant type,
        EzyEvent event,
        String username,
        boolean catchException
    );

    void broadcastApps(
        EzyConstant type,
        EzyEvent event,
        EzyUser user,
        boolean catchException
    );

    void broadcastApps(
        EzyConstant type,
        EzyEvent event,
        Predicate<EzyAppContext> filter,
        boolean catchException
    );
}
