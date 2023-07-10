package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.controller.EzyAbstractServerEventController;
import com.tvd12.ezyfoxserver.event.EzySimpleServerInitializingEvent;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSettingBuilder.EzyMaxRequestPerSecondBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSessionManagementSetting.EzySimpleMaxRequestPerSecond;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EzySettingsBuilderTest {

    @Test
    public void test() {
        EzySimpleStreamingSetting streamingSetting = new EzySimpleStreamingSetting();
        EzySimpleHttpSetting httpSetting = new EzySimpleHttpSetting();
        EzySimpleSocketSetting socketSetting = new EzySocketSettingBuilder()
            .active(true)
            .address("1.1.1.1")
            .codecCreator(TestCodecCreator.class)
            .maxRequestSize(1024)
            .port(12345)
            .tcpNoDelay(true)
            .writerThreadPoolSize(3)
            .build();
        EzySimpleUdpSetting udpSetting = new EzyUdpSettingBuilder()
            .active(true)
            .address("2.2.2.2")
            .channelPoolSize(3)
            .handlerThreadPoolSize(3)
            .maxRequestSize(2048)
            .port(23456)
            .build();
        EzySimpleSslConfigSetting sslConfigSetting = new EzySimpleSslConfigSetting();
        EzySimpleWebSocketSetting webSocketSetting = new EzyWebSocketSettingBuilder()
            .active(true)
            .address("1.1.1.1")
            .codecCreator(TestCodecCreator.class)
            .maxFrameSize(1024)
            .port(12345)
            .sslActive(true)
            .sslConfig(sslConfigSetting)
            .sslPort(23456)
            .writerThreadPoolSize(3)
            .build();
        EzySimpleAdminsSetting adminsSetting = new EzySimpleAdminsSetting();
        EzySimpleAdminSetting adminSetting = new EzyAdminSettingBuilder()
            .accessToken("123")
            .username("admin")
            .password("123456")
            .build();
        EzySimpleLoggerSetting loggerSetting = new EzySimpleLoggerSetting();
        EzySimpleThreadPoolSizeSetting threadPoolSizeSetting = new EzyThreadPoolSizeSettingBuilder()
            .socketDataReceiver(1)
            .extensionRequestHandler(2)
            .socketDisconnectionHandler(3)
            .socketUserRemovalHandler(4)
            .statistics(5)
            .streamHandler(6)
            .systemRequestHandler(7)
            .build();
        EzySimpleMaxRequestPerSecond maxRequestPerSecond = new EzyMaxRequestPerSecondBuilder()
            .value(15)
            .action(EzyMaxRequestPerSecondAction.DROP_REQUEST)
            .build();
        EzySimpleSessionManagementSetting sessionManagementSetting = new EzySessionManagementSettingBuilder()
            .sessionMaxIdleTimeInSecond(100)
            .sessionMaxRequestPerSecond(maxRequestPerSecond)
            .sessionMaxWaitingTimeInSecond(200)
            .build();
        EzySimpleEventControllersSetting eventControllersSetting = new EzySimpleEventControllersSetting();
        EzySimpleZonesSetting zonesSetting = new EzySimpleZonesSetting();
        EzySimpleZoneSetting zoneSetting = new EzyZoneSettingBuilder()
            .name("test")
            .build();
        EzySimpleSettings settings = new EzySettingsBuilder()
            .debug(true)
            .nodeName("test")
            .maxSessions(100)
            .socket(socketSetting)
            .udp(udpSetting)
            .streaming(streamingSetting)
            .http(httpSetting)
            .websocket(webSocketSetting)
            .admins(adminsSetting)
            .admin(adminSetting)
            .logger(loggerSetting)
            .threadPoolSize(threadPoolSizeSetting)
            .sessionManagement(sessionManagementSetting)
            .eventControllers(eventControllersSetting)
            .zones(zonesSetting)
            .zone(zoneSetting)
            .addEventController(EzyEventType.SERVER_INITIALIZING, HelloServerInitializingReadyController.class)
            .build();
        assertTrue(settings.isDebug());
        assertEquals(settings.getNodeName(), "test");
        assertEquals(settings.getMaxSessions(), 100);
        assertEquals(settings.getStreaming(), streamingSetting);
        assertEquals(settings.getHttp(), httpSetting);

        socketSetting = settings.getSocket();
        assertTrue(socketSetting.isActive());
        assertEquals(socketSetting.getAddress(), "1.1.1.1");
        assertEquals(socketSetting.getCodecCreator(), TestCodecCreator.class.getName());
        assertEquals(socketSetting.getMaxRequestSize(), 1024);
        assertEquals(socketSetting.getPort(), 12345);
        assertTrue(socketSetting.isTcpNoDelay());
        assertEquals(socketSetting.getWriterThreadPoolSize(), 3);

        udpSetting = settings.getUdp();
        assertTrue(udpSetting.isActive());
        assertEquals(udpSetting.getAddress(), "2.2.2.2");
        assertEquals(udpSetting.getMaxRequestSize(), 2048);
        assertEquals(udpSetting.getPort(), 23456);
        assertEquals(udpSetting.getChannelPoolSize(), 3);
        assertEquals(udpSetting.getHandlerThreadPoolSize(), 3);

        webSocketSetting = settings.getWebsocket();
        assertTrue(webSocketSetting.isActive());
        assertEquals(webSocketSetting.getAddress(), "1.1.1.1");
        assertEquals(webSocketSetting.getCodecCreator(), TestCodecCreator.class.getName());
        assertEquals(webSocketSetting.getMaxFrameSize(), 1024);
        assertEquals(webSocketSetting.getPort(), 12345);
        assertTrue(webSocketSetting.isSslActive());
        assertEquals(webSocketSetting.getSslConfig(), sslConfigSetting);
        assertEquals(webSocketSetting.getSslPort(), 23456);
        assertEquals(webSocketSetting.getWriterThreadPoolSize(), 3);

        assertEquals(settings.getAdmins(), adminsSetting);

        adminSetting = (EzySimpleAdminSetting) settings.getAdmins().getAdminByName("admin");
        assertEquals(adminSetting.getAccessToken(), "123");
        assertEquals(adminSetting.getUsername(), "admin");
        assertEquals(adminSetting.getPassword(), "123456");

        assertEquals(settings.getLogger(), loggerSetting);

        threadPoolSizeSetting = settings.getThreadPoolSize();
        assertEquals(threadPoolSizeSetting.getSocketDataReceiver(), 1);
        assertEquals(threadPoolSizeSetting.getExtensionRequestHandler(), 2);
        assertEquals(threadPoolSizeSetting.getSocketDisconnectionHandler(), 3);
        assertEquals(threadPoolSizeSetting.getSocketUserRemovalHandler(), 4);
        assertEquals(threadPoolSizeSetting.getStatistics(), 5);
        assertEquals(threadPoolSizeSetting.getStreamHandler(), 6);
        assertEquals(threadPoolSizeSetting.getSystemRequestHandler(), 7);

        sessionManagementSetting = settings.getSessionManagement();
        assertEquals(sessionManagementSetting.getSessionMaxIdleTimeInSecond(), 100);
        assertEquals(sessionManagementSetting.getSessionMaxRequestPerSecond(), maxRequestPerSecond);
        assertEquals(maxRequestPerSecond.getValue(), 15);
        assertEquals(maxRequestPerSecond.getAction(), EzyMaxRequestPerSecondAction.DROP_REQUEST);

        assertEquals(settings.getEventControllers(), eventControllersSetting);

        assertEquals(settings.getZones().getSize(), 1);

        assertEquals(zonesSetting.getZoneByName("test"), zoneSetting);
    }

    @Test
    public void initCallTest() {
        // given
        int sessionMaxIdleTimeInSecond = RandomUtil.randomSmallInt();
        int sessionMaxWaitingTimeInSecond = RandomUtil.randomSmallInt();

        EzySessionManagementSettingBuilder sessionManagementSettingBuilder =
            new EzySessionManagementSettingBuilder()
                .sessionMaxIdleTimeInSecond(sessionMaxIdleTimeInSecond)
                .sessionMaxWaitingTimeInSecond(sessionMaxWaitingTimeInSecond);

        int userMaxIdleTimeInSecond = RandomUtil.randomSmallInt();
        EzyUserManagementSettingBuilder userManagementSettingBuilder =
            new EzyUserManagementSettingBuilder()
                .userMaxIdleTimeInSecond(userMaxIdleTimeInSecond);

        String appName = RandomUtil.randomShortAlphabetString();
        String appPackageName = RandomUtil.randomShortAlphabetString();
        EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
            .name(appName)
            .packageName(appPackageName);

        String pluginName = RandomUtil.randomShortAlphabetString();
        String pluginPackageName = RandomUtil.randomShortAlphabetString();
        EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
            .name(pluginName)
            .packageName(pluginPackageName);

        String zoneName = RandomUtil.randomShortAlphabetString();
        EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
            .name(zoneName)
            .userManagement(userManagementSettingBuilder.build())
            .application(appSettingBuilder.build())
            .plugin(pluginSettingBuilder.build());

        // when
        EzySettings settings = new EzySettingsBuilder()
            .sessionManagement(sessionManagementSettingBuilder.build())
            .zone(zoneSettingBuilder.build())
            .build();

        // then
        Asserts.assertEquals(
            settings.getSessionManagement().getSessionMaxIdleTime(),
            sessionMaxIdleTimeInSecond * 1000,
            false
        );
        Asserts.assertEquals(
            settings.getSessionManagement().getSessionMaxWaitingTime(),
            sessionMaxWaitingTimeInSecond * 1000,
            false
        );
        Asserts.assertEquals(
            settings.getZoneByName(zoneName).getUserManagement().getUserMaxIdleTime(),
            userMaxIdleTimeInSecond * 1000,
            false
        );
        Asserts.assertEquals(
            settings.getZoneByName(zoneName).getAppByName(appName).getZoneId(),
            settings.getZoneByName(zoneName).getId()
        );
        Asserts.assertEquals(
            settings.getZoneByName(zoneName).getAppByName(appName).getPackageName(),
            appPackageName
        );
        Asserts.assertEquals(
            settings.getZoneByName(zoneName).getPluginByName(pluginName).getZoneId(),
            settings.getZoneByName(zoneName).getId()
        );
        Asserts.assertEquals(
            settings.getZoneByName(zoneName).getPluginByName(pluginName).getPackageName(),
            pluginPackageName
        );
    }

    public static class TestCodecCreator implements EzyCodecCreator {

        @Override
        public Object newEncoder() {
            return null;
        }

        @Override
        public Object newDecoder(int maxRequestSize) {
            return null;
        }

    }

    public static class HelloServerInitializingReadyController
        extends EzyAbstractServerEventController<EzySimpleServerInitializingEvent> {

        @Override
        public void handle(EzyServerContext ctx, EzySimpleServerInitializingEvent event) {
            // add logic here
        }

    }
}
