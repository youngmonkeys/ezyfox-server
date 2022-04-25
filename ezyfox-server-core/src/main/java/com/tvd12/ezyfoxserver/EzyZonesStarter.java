package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzyServerInitializingEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;

import java.util.Map;
import java.util.Set;

public class EzyZonesStarter extends EzyComponentsStater {

    protected EzyZonesStarter(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void start() {
        startAllZones();
    }

    protected void startAllZones() {
        Set<String> zoneNames = getZoneNames();
        logger.info("start zones: {}", zoneNames);
        for (String zoneName : zoneNames) {
            this.startZone(zoneName);
        }
    }

    protected void startZone(String zoneName) {
        EzyZoneContext zoneContext = serverContext.getZoneContext(zoneName);
        notifyServerInitializing(zoneContext);
        startAllPlugins(zoneContext);
        startAllApps(zoneContext);
    }

    protected void notifyServerInitializing(EzyZoneContext zoneContext) {
        EzyServerInitializingEvent event = new EzySimpleServerInitializingEvent();
        zoneContext.handleEvent(EzyEventType.SERVER_INITIALIZING, event);
    }

    //===================== plugins ===================
    protected void startAllPlugins(EzyZoneContext zoneContext) {
        logger.info("start all plugins ...");
        startComponents(newPluginsStarterBuilder(), zoneContext);
    }

    //=================================================

    protected EzyPluginsStarter.Builder newPluginsStarterBuilder() {
        return EzyPluginsStarter.builder();
    }

    //====================== apps ===================
    protected void startAllApps(EzyZoneContext zoneContext) {
        logger.info("start all apps ...");
        EzyServer server = serverContext.getServer();
        EzyAppsStarter.Builder appsStarterBuilder = newAppsStarterBuilder()
            .classLoader(server.getClassLoader())
            .appClassLoaders(getAppClassLoaders())
            .enableAppClassLoader(server.getConfig().isEnableAppClassLoader());
        startComponents(appsStarterBuilder, zoneContext);
    }
    //=================================================

    protected EzyAppsStarter.Builder newAppsStarterBuilder() {
        return EzyAppsStarter.builder();
    }

    protected void startComponents(
        EzyZoneComponentsStater.Builder<?, ?> builder, EzyZoneContext zoneContext) {
        EzyZoneComponentsStater starter = newComponentsStater(builder, zoneContext);
        starter.start();
    }

    protected EzyZoneComponentsStater newComponentsStater(
        EzyZoneComponentsStater.Builder<?, ?> builder, EzyZoneContext zoneContext) {
        return builder
            .zoneContext(zoneContext)
            .build();
    }

    protected Set<String> getZoneNames() {
        return settings.getZoneNames();
    }

    protected Map<String, ClassLoader> getAppClassLoaders() {
        return serverContext.getServer().getAppClassLoaders();
    }

    public static class Builder extends EzyComponentsStater.Builder<EzyZonesStarter, Builder> {

        @Override
        public EzyZonesStarter build() {
            return new EzyZonesStarter(this);
        }
    }
}
