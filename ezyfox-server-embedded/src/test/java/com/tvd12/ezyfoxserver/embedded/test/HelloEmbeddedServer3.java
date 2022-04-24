package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfox.bean.EzyBeanContextBuilder;
import com.tvd12.ezyfox.codec.JacksonCodecCreator;
import com.tvd12.ezyfox.codec.MsgPackCodecCreator;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerEventController;
import com.tvd12.ezyfoxserver.controller.EzyAbstractZoneEventController;
import com.tvd12.ezyfoxserver.embedded.EzyEmbeddedServer;
import com.tvd12.ezyfoxserver.event.EzyServerReadyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSettingBuilder.EzyMaxRequestPerSecondBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.ezyfoxserver.setting.EzySimplePluginSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUdpSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUserManagementSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleZoneSetting;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUserManagementSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class HelloEmbeddedServer3 {

    public static void main(String[] args) throws Exception {
        EzySimplePluginSetting pluginSetting = new EzyPluginSettingBuilder()
                .name("hello") // plugin name
                .addListenEvent(EzyEventType.USER_LOGIN) // listen able events USER_LOGIN, USER_ADDED, USER_REMOVED
                .configFile("config.properties")
                .entryLoader(HelloPluginEntryLoader.class)
//                .entryLoaderArgs() // pass arguments to entry loader constructor
                .priority(1) // set priority, bigger number is lower, default 0
                .threadPoolSize(3) // set thread pool size to create executor service for this plugin, default 0
                .build();

        EzySimpleAppSetting appSetting = new EzyAppSettingBuilder()
                .name("hello") // app's name
                .configFile("config.properties")
                .entryLoader(HelloAppEntryLoader.class)
                .maxUsers(9999) // set max user in this app, default 999999
//                .entryLoaderArgs() // pass arguments to entry loader constructor
                .threadPoolSize(3) // set thread pool size to create executor service for this app, default 0
                .build();

        EzySimpleUserManagementSetting userManagementSetting = new EzyUserManagementSettingBuilder()
                .allowChangeSession(true) // allow change user's session, default true
                .allowGuestLogin(true) // allow guest login, default false
                .guestNamePrefix("Guest#") // set name prefix for guest
                .maxSessionPerUser(5) // set number of max sessions per user // default 5
                .userMaxIdleTimeInSecond(15) // set max idle time of an user, default 0
                .userNamePattern("^[a-z0-9_.]{3,36}$") // set username pattern, default ^[a-z0-9_.]{3,36}$
                .build();

        EzySimpleZoneSetting zoneSetting = new EzyZoneSettingBuilder()
                .name("hello") // zone's name
                .plugin(pluginSetting) // add a plug-in to zone
                .application(appSetting) // add an app to zone
                .configFile("config.properties") // set config file
                .maxUsers(999999) // set maximum user for zone
                .userManagement(userManagementSetting) // set user management settings
                 // add event controller, accept SERVER_INITIALIZING, SERVER_READY
                .addEventController(EzyEventType.SERVER_READY, HelloZoneServerReadyController.class)
                .build();

        EzySimpleSocketSetting socketSetting = new EzySocketSettingBuilder()
                .active(true) // active or not,  default true
                .address("0.0.0.0") // loopback address, default 0.0.0.0
                .codecCreator(MsgPackCodecCreator.class) // encoder/decoder creator, default MsgPackCodecCreator
                .maxRequestSize(1024) // max request size, default 32768
                .port(3005) // port, default 3005
                .tcpNoDelay(true) // tcp no delay, default false
                .writerThreadPoolSize(8) // thread pool size for socket writer, default 8
                .build();

        EzySimpleWebSocketSetting webSocketSetting = new EzyWebSocketSettingBuilder()
                .active(true) // active or not,  default true
                .address("0.0.0.0") // loopback address, default 0.0.0.0
                .codecCreator(JacksonCodecCreator.class) // encoder/decoder creator, default JacksonCodecCreator
                .maxFrameSize(32678) // max frame size, default 32768
                .port(2208) // port, default 3005
                .writerThreadPoolSize(8) // thread pool size for socket writer, default 8
                .build();

        EzySimpleMaxRequestPerSecond maxRequestPerSecond = new EzyMaxRequestPerSecondBuilder()
                .value(15) // max request in a second
                .action(EzyMaxRequestPerSecondAction.DROP_REQUEST) // action when get max
                .build();

        EzySimpleSessionManagementSetting sessionManagementSetting = new EzySessionManagementSettingBuilder()
                .sessionMaxIdleTimeInSecond(30) // set max idle time for session, default 30s
                .sessionMaxWaitingTimeInSecond(30) // set max waiting time to login for session, default 30s
                .sessionMaxRequestPerSecond(maxRequestPerSecond) // set max request in a session for a session
                .build();

        EzySimpleUdpSetting udpSetting = new EzyUdpSettingBuilder()
                .active(true) // active or not
                .address("0.0.0.0") // set loopback IP
                .channelPoolSize(16) // set number of udp channel for socket writing, default 16
                .codecCreator(MsgPackCodecCreator.class) // encoder/decoder creator, default MsgPackCodecCreator
                .handlerThreadPoolSize(5) // set number of handler's thread, default 5
                .maxRequestSize(1024) // set max request's size
                .port(2611) // set listen port
                .build();

        EzySimpleSettings settings = new EzySettingsBuilder()
                .debug(true) // allow debug to print log or not, default false
                .nodeName("hello") // for convenient
                .zone(zoneSetting) // add a zone to server
                .socket(socketSetting) // set socket setting
                .websocket(webSocketSetting) // set websocket setting
                .udp(udpSetting) // set udp setting
                .sessionManagement(sessionManagementSetting) // set session management setting
                // add event controller, accept SERVER_INITIALIZING, SERVER_READY
                .addEventController(EzyEventType.SERVER_INITIALIZING, HelloServerInitializingReadyController.class)
                .build();

        EzyEmbeddedServer server = EzyEmbeddedServer.builder()
                .settings(settings)
                .build();
        server.start();
    }

    public static class HelloAppEntry extends EzySimpleAppEntry {

        // packages to scan bean
        @Override
        protected String[] getScanableBeanPackages() {
            return new String[] {
                    "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
            };
        }

        // packages to scan POJO mapped socket binary data
        @Override
        protected String[] getScanableBindingPackages() {
            return new String[] {
                    "com.tvd12.ezyfoxserver.embedded.test" // replace by your package
            };
        }

        @Override
        protected void setupBeanContext(EzyAppContext context, EzyBeanContextBuilder builder) {
            // register bean here
        }

    }

    public static class HelloAppEntryLoader extends EzyAbstractAppEntryLoader {

        @Override
        public EzyAppEntry load() throws Exception {
            return new HelloAppEntry();
        }

    }

    public static class HelloPluginEntry extends EzySimplePluginEntry {

        // packages to scan bean
        @Override
        protected String[] getScanableBeanPackages() {
            return new String[] {
                    "com.tvd12.ezyfoxserver.embedded.test.plugin" // replace by your package
            };
        }

    }

    public static class HelloPluginEntryLoader extends EzyAbstractPluginEntryLoader {

        @Override
        public EzyPluginEntry load() throws Exception {
            return new HelloPluginEntry();
        }

    }

    public static class HelloServerInitializingReadyController
            extends EzyAbstractServerEventController<EzySimpleServerInitializingEvent> {

        @Override
        public void handle(EzyServerContext ctx, EzySimpleServerInitializingEvent event) {
            // add logic here
        }

    }

    public static class HelloZoneServerReadyController
            extends EzyAbstractZoneEventController<EzyServerReadyEvent> {

        @Override
        public void handle(EzyZoneContext ctx, EzyServerReadyEvent event) {
            // add logic here
        }

    }

}
