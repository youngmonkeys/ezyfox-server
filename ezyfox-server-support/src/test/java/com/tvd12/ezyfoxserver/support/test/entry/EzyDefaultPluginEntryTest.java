package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.concurrent.EzyErrorScheduledExecutorService;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.EzySimplePlugin;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.EzySimpleZone;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzySimplePluginContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.support.entry.EzyDefaultPluginEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import org.testng.annotations.Test;

import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.spy;

public class EzyDefaultPluginEntryTest {

    @Test
    public void test() throws Exception {
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
        zoneContext.setZone(zone);
        zoneContext.init();
        zoneContext.setParent(serverContext);

        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("test");
        pluginSetting.setActiveProfiles("hello,world");
        pluginSetting.setPackageName("x.z.y");

        EzyEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
        EzyEventControllers eventControllers = EzyEventControllersImpl.create(eventControllersSetting);
        EzySimplePlugin plugin = new EzySimplePlugin();
        plugin.setSetting(pluginSetting);
        plugin.setEventControllers(eventControllers);

        ScheduledExecutorService pluginScheduledExecutorService = new EzyErrorScheduledExecutorService("not implement");
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setPlugin(plugin);
        pluginContext.setParent(zoneContext);
        pluginContext.setExecutorService(pluginScheduledExecutorService);
        pluginContext.init();

        EzySimplePluginEntry entry = new EzyPluginEntryEx();
        entry.config(pluginContext);
        entry.start();
        handleClientRequest(pluginContext);
        entry.destroy();
    }

    private void handleClientRequest(EzyPluginContext context) {
        EzySimplePlugin plugin = (EzySimplePlugin) context.getPlugin();
        EzyPluginRequestController requestController = plugin.getRequestController();

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        EzyArray data = EzyEntityFactory.newArrayBuilder()
            .append("chat")
            .append(EzyEntityFactory.newObjectBuilder()
                .append("message", "greet"))
            .build();
        EzyUserRequestPluginEvent event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("chat")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("no command")
            .append(EzyEntityFactory.newObjectBuilder()
                .append("message", "greet"))
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("noUser")
            .append(EzyEntityFactory.newObjectBuilder()
                .append("message", "greet"))
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("noSession")
            .append(EzyEntityFactory.newObjectBuilder()
                .append("message", "greet"))
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("noDataBinding")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("badRequestSend")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("badRequestNoSend")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
            .append("exception")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        try {
            requestController.handle(context, event);
        } catch (Exception e) {
            assert e instanceof IllegalStateException;
        }

        data = EzyEntityFactory.newArrayBuilder()
            .append("plugin")
            .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);
    }

    @Test
    public void test2() throws Exception {
        EzySimpleSettings settings = new EzySimpleSettings();
        EzySimpleServer server = new EzySimpleServer();
        server.setSettings(settings);
        EzySimpleServerContext serverContext = new EzySimpleServerContext();
        serverContext.setServer(server);
        serverContext.init();

        EzySimpleZoneSetting zoneSetting = new EzySimpleZoneSetting();
        EzySimpleZone zone = new EzySimpleZone();
        zone.setSetting(zoneSetting);
        EzySimpleZoneContext zoneContext = new EzySimpleZoneContext();
        zoneContext.setZone(zone);
        zoneContext.init();
        zoneContext.setParent(serverContext);

        EzySimplePluginSetting pluginSetting = new EzySimplePluginSetting();
        pluginSetting.setName("test");

        EzyEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
        EzyEventControllers eventControllers = EzyEventControllersImpl.create(eventControllersSetting);
        EzySimplePlugin plugin = new EzySimplePlugin();
        plugin.setSetting(pluginSetting);
        plugin.setEventControllers(eventControllers);

        ScheduledExecutorService pluginScheduledExecutorService = new EzyErrorScheduledExecutorService("not implement");
        EzySimplePluginContext pluginContext = new EzySimplePluginContext();
        pluginContext.setPlugin(plugin);
        pluginContext.setParent(zoneContext);
        pluginContext.setExecutorService(pluginScheduledExecutorService);
        pluginContext.init();

        EzySimplePluginEntry entry = new EzyPluginEntryEx2();
        entry.config(pluginContext);
        entry.start();
        entry.destroy();
    }

    public static class EzyPluginEntryEx extends EzyDefaultPluginEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[]{
                "com.tvd12.ezyfoxserver.support.test.entry"
            };
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected Class[] getPrototypeClasses() {
            return new Class[]{
                ClientPluginRequestHandler.class
            };
        }

        @Override
        protected String[] getScanableBindingPackages() {
            return new String[]{
                "com.tvd12.ezyfoxserver.support.test.entry"
            };
        }

        @Override
        protected void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder) {
        }

    }

    public static class EzyPluginEntryEx2 extends EzyDefaultPluginEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[0];
        }

        @Override
        protected void setupBeanContext(EzyPluginContext context, EzyBeanContextBuilder builder) {
        }

    }
}
