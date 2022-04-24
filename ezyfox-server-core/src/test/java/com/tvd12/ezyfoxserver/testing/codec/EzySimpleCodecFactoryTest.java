package com.tvd12.ezyfoxserver.testing.codec;

import com.tvd12.ezyfox.codec.*;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.setting.EzySimpleSocketSetting;
import com.tvd12.ezyfoxserver.setting.EzySimpleWebSocketSetting;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzySimpleCodecFactoryTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSocketSetting socketSetting = new EzySimpleSocketSetting();
        socketSetting.setActive(false);
        EzySimpleWebSocketSetting webSocketSetting = new EzySimpleWebSocketSetting();
        webSocketSetting.setActive(false);
        EzyCodecFactory factory = EzySimpleCodecFactory.builder()
            .socketSetting(socketSetting)
            .websocketSetting(webSocketSetting)
            .build();
        assert factory.newEncoder(EzyConnectionType.SOCKET) == null;
        assert factory.newEncoder(EzyConnectionType.WEBSOCKET) == null;
        assert factory.newDecoder(EzyConnectionType.SOCKET) == null;
        assert factory.newDecoder(EzyConnectionType.WEBSOCKET) == null;

        socketSetting.setActive(true);
        socketSetting.setCodecCreator(ExBytesCodecCreator.class.getName());
        webSocketSetting.setActive(true);
        webSocketSetting.setCodecCreator(ExStringCodecCreator.class.getName());

        factory = EzySimpleCodecFactory.builder()
            .socketSetting(socketSetting)
            .websocketSetting(webSocketSetting)
            .build();

        assert factory.newEncoder(EzyConnectionType.SOCKET) != null;
        assert factory.newEncoder(EzyConnectionType.WEBSOCKET) != null;
        assert factory.newDecoder(EzyConnectionType.SOCKET) != null;
        assert factory.newDecoder(EzyConnectionType.WEBSOCKET) != null;
    }

    public static class ExBytesCodecCreator implements EzyCodecCreator {

        @Override
        public EzyByteToObjectDecoder newDecoder(int maxRequestSize) {
            EzyByteToObjectDecoder decoder = mock(EzyByteToObjectDecoder.class);
            return decoder;
        }

        @Override
        public EzyObjectToByteEncoder newEncoder() {
            EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
            return encoder;
        }

    }

    public static class ExStringCodecCreator implements EzyCodecCreator {

        @Override
        public Object newEncoder() {
            EzyObjectToStringEncoder encoder = mock(EzyObjectToStringEncoder.class);
            return encoder;
        }

        @Override
        public Object newDecoder(int maxRequestSize) {
            EzyStringToObjectDecoder decoder = mock(EzyStringToObjectDecoder.class);
            return decoder;
        }

    }

}
