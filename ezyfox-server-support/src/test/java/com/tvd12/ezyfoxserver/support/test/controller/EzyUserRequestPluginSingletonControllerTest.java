package com.tvd12.ezyfoxserver.support.test.controller;

import static org.mockito.Mockito.spy;

import java.util.concurrent.ScheduledExecutorService;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.bean.EzyBeanContext;
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
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.support.controller.EzyUserRequestPluginSingletonController;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;
import com.tvd12.ezyfoxserver.support.manager.EzyFeatureCommandManager;
import com.tvd12.ezyfoxserver.support.manager.EzyRequestCommandManager;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyZoneUserManagerImpl;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class EzyUserRequestPluginSingletonControllerTest extends BaseTest {

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
        EzyZoneUserManager zoneUserManager = EzyZoneUserManagerImpl.builder()
                .zoneName("test")
                .build();
        zone.setUserManager(zoneUserManager);
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

        EzySimplePluginEntry entry = new EzyPluginEntryEx();
        entry.config(pluginContext);
        entry.start();
        handleClientRequest(pluginContext);
        EzyBeanContext beanContext = pluginContext.get(EzyBeanContext.class);
        EzyRequestCommandManager requestCommandManager =
            beanContext.getSingleton(EzyRequestCommandManager.class);
        EzyFeatureCommandManager featureCommandManager =
            beanContext.getSingleton(EzyFeatureCommandManager.class);
        Asserts.assertTrue(requestCommandManager.containsCommand("v1.2.2/hello"));
        Asserts.assertTrue(requestCommandManager.isManagementCommand("v1.2.2/hello"));
        Asserts.assertTrue(requestCommandManager.isPaymentCommand("v1.2.2/hello"));
        Asserts.assertEquals(featureCommandManager.getFeatureByCommand("v1.2.2/hello"), "hello.world");
        entry.destroy();
    }

    private void handleClientRequest(EzyPluginContext context) {
        EzySimplePlugin plugin = (EzySimplePlugin) context.getPlugin();
        EzyPluginRequestController requestController = plugin.getRequestController();

        EzyAbstractSession session = spy(EzyAbstractSession.class);
        EzySimpleUser user = new EzySimpleUser();
        EzyArray data = EzyEntityFactory.newArrayBuilder()
                .append("hello")
                .append(EzyEntityFactory.newObjectBuilder()
                        .append("who", "Mr.Young Monkey!"))
                .build();
        EzyUserRequestPluginEvent event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("responseFactoryTest")
                .append(EzyEntityFactory.newObjectBuilder()
                        .append("who", "Mr.Young Monkey!"))
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("no command")
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("hello2")
                .append(EzyEntityFactory.newObjectBuilder()
                        .append("who", "Mr.Young Monkey!"))
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("plugin/c_hello")
                .append(EzyEntityFactory.newObjectBuilder()
                        .append("who", "Mr.Young Monkey!"))
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("badRequestSend")
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("badRequestNotSend")
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        data = EzyEntityFactory.newArrayBuilder()
                .append("plugin/requestException4")
                .append(EzyEntityFactory.newObjectBuilder()
                        .append("who", "Mr.Young Monkey!"))
                .build();
        event = new EzySimpleUserRequestPluginEvent(user, session, data);
        requestController.handle(context, event);

        try {
            data = EzyEntityFactory.newArrayBuilder()
                    .append("exception")
                    .build();
            event = new EzySimpleUserRequestPluginEvent(user, session, data);
            requestController.handle(context, event);
        }
        catch (Exception e) {
            assert e instanceof IllegalStateException;
        }
    }

    public static class EzyPluginEntryEx extends EzySimplePluginEntry {

        @Override
        protected String[] getScanableBeanPackages() {
            return new String[] {
                    "com.tvd12.ezyfoxserver.support.test.controller.plugin"
            };
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected Class[] getPrototypeClasses() {
            return new Class[] {
            };
        }

        @Override
        protected String[] getScanableBindingPackages() {
            return new String[] {
                    "com.tvd12.ezyfoxserver.support.test.controller"
            };
        }

        @Override
        protected EzyPluginRequestController newUserRequestController(EzyBeanContext beanContext) {
            return EzyUserRequestPluginSingletonController.builder()
                    .beanContext(beanContext)
                    .build();
        }

    }
}
