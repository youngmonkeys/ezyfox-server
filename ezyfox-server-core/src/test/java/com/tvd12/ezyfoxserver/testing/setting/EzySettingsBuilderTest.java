package com.tvd12.ezyfoxserver.testing.setting;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.setting.EzyAdminSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleAdminSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleHttpSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleLoggerSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleSslConfigSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleStreamingSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleThreadPoolSizeSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleUdpSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySocketSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyThreadPoolSizeSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyUdpSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyWebSocketSettingBuilder;

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
                .codecCreator(TestCodecCreator.class)
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
        EzySimpleAdminSetting adminSetting = new EzyAdminSettingBuilder()
                .accessToken("123")
                .username("admin")
                .password("123456")
                .build();
        EzySimpleLoggerSetting loggerSetting = new EzySimpleLoggerSetting();
        EzySimpleThreadPoolSizeSetting threadPoolSizeSetting = new EzyThreadPoolSizeSettingBuilder()
                .codec(1)
                .extensionRequestHandler(2)
                .socketDisconnectionHandler(3)
                .socketUserRemovalHandler(4)
                .statistics(5)
                .streamHandler(6)
                .systemRequestHandler(7)
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
                .admin(adminSetting)
                .logger(loggerSetting)
                .threadPoolSize(threadPoolSizeSetting)
                .build();
        assertEquals(settings.isDebug(), true);
        assertEquals(settings.getNodeName(), "test");
        assertEquals(settings.getMaxSessions(), 100);
        assertEquals(settings.getStreaming(), streamingSetting);
        assertEquals(settings.getHttp(), httpSetting);
        
        socketSetting = settings.getSocket();
        assertEquals(socketSetting.isActive(), true);
        assertEquals(socketSetting.getAddress(), "1.1.1.1");
        assertEquals(socketSetting.getCodecCreator(), TestCodecCreator.class.getName());
        assertEquals(socketSetting.getMaxRequestSize(), 1024);
        assertEquals(socketSetting.getPort(), 12345);
        assertEquals(socketSetting.isTcpNoDelay(), true);
        assertEquals(socketSetting.getWriterThreadPoolSize(), 3);
        
        udpSetting = settings.getUdp();
        assertEquals(udpSetting.isActive(), true);
        assertEquals(udpSetting.getAddress(), "2.2.2.2");
        assertEquals(udpSetting.getCodecCreator(), TestCodecCreator.class.getName());
        assertEquals(udpSetting.getMaxRequestSize(), 2048);
        assertEquals(udpSetting.getPort(), 23456);
        assertEquals(udpSetting.getChannelPoolSize(), 3);
        assertEquals(udpSetting.getHandlerThreadPoolSize(), 3);
        
        webSocketSetting = settings.getWebsocket();
        assertEquals(webSocketSetting.isActive(), true);
        assertEquals(webSocketSetting.getAddress(), "1.1.1.1");
        assertEquals(webSocketSetting.getCodecCreator(), TestCodecCreator.class.getName());
        assertEquals(webSocketSetting.getMaxFrameSize(), 1024);
        assertEquals(webSocketSetting.getPort(), 12345);
        assertEquals(webSocketSetting.isSslActive(), true);
        assertEquals(webSocketSetting.getSslConfig(), sslConfigSetting);
        assertEquals(webSocketSetting.getSslPort(), 23456);
        assertEquals(webSocketSetting.getWriterThreadPoolSize(), 3);
        
        adminSetting = (EzySimpleAdminSetting) settings.getAdmins().getAdminByName("admin");
        assertEquals(adminSetting.getAccessToken(), "123");
        assertEquals(adminSetting.getUsername(), "admin");
        assertEquals(adminSetting.getPassword(), "123456");
        
        assertEquals(settings.getLogger(), loggerSetting);
        
        threadPoolSizeSetting = settings.getThreadPoolSize();
        assertEquals(threadPoolSizeSetting.getCodec(), 1);
        assertEquals(threadPoolSizeSetting.getExtensionRequestHandler(), 2);
        assertEquals(threadPoolSizeSetting.getSocketDisconnectionHandler(), 3);
        assertEquals(threadPoolSizeSetting.getSocketUserRemovalHandler(), 4);
        assertEquals(threadPoolSizeSetting.getStatistics(), 5);
        assertEquals(threadPoolSizeSetting.getStreamHandler(), 6);
        assertEquals(threadPoolSizeSetting.getSystemRequestHandler(), 7);
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
}
