package com.tvd12.ezyfoxserver.testing.codec;

import com.tvd12.ezyfox.codec.*;
import com.tvd12.ezyfox.codec.EzyStringToObjectDecoder;
import com.tvd12.ezyfox.constant.EzyConstant;
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
        EzyConstant otherType = EzyConstant.one("OTHER");
        assert factory.newEncoder(otherType) == null;
        assert factory.newDecoder(EzyConnectionType.SOCKET) == null;
        assert factory.newDecoder(EzyConnectionType.WEBSOCKET) == null;
        assert factory.newDecoder(otherType) == null;

        // The WebSocket branch is selected, but its creator is absent while inactive.
        webSocketSetting.setActive(true);
        assert factory.newEncoder(EzyConnectionType.WEBSOCKET) == null;
        assert factory.newDecoder(EzyConnectionType.WEBSOCKET) == null;

        socketSetting.setActive(true);
        socketSetting.setCodecCreator(ExBytesCodecCreator.class.getName());
        webSocketSetting.setCodecCreator(ExStringCodecCreator.class.getName());
        webSocketSetting.setMaxFrameSize(8192);

        factory = EzySimpleCodecFactory.builder()
            .socketSetting(socketSetting)
            .websocketSetting(webSocketSetting)
            .build();

        assert factory.newEncoder(EzyConnectionType.SOCKET) != null;
        assert factory.newEncoder(EzyConnectionType.WEBSOCKET)
            instanceof EzyObjectToStringEncoder;
        assert factory.newDecoder(EzyConnectionType.SOCKET) != null;
        EzyStringToObjectDecoder websocketDecoder =
            (EzyStringToObjectDecoder) factory.newDecoder(EzyConnectionType.WEBSOCKET);
        assert websocketDecoder != null;
        assert ExStringCodecCreator.maxFrameSize == 8192;
    }

    public static class ExBytesCodecCreator implements EzyCodecCreator {

        @Override
        public EzyByteToObjectDecoder newDecoder(int maxRequestSize) {
            return mock(EzyByteToObjectDecoder.class);
        }

        @Override
        public EzyObjectToByteEncoder newEncoder() {
            return mock(EzyObjectToByteEncoder.class);
        }

    }

    public static class ExStringCodecCreator implements EzyCodecCreator {

        private static int maxFrameSize;

        @Override
        public Object newEncoder() {
            return mock(EzyObjectToStringEncoder.class);
        }

        @Override
        public Object newDecoder(int maxRequestSize) {
            maxFrameSize = maxRequestSize;
            return mock(EzyStringToObjectDecoder.class);
        }

    }
}
