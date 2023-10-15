package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfoxserver.api.EzySecureProxyResponseApi;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzySecureProxyResponseApiTest {

    @Test
    public void responseTest() throws Exception {
        // given
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        EzyObjectToByteEncoder encoder = mock(EzyObjectToByteEncoder.class);
        when(codecFactory.newEncoder(EzyConnectionType.SOCKET)).thenReturn(encoder);

        EzySecureProxyResponseApi api = new EzySecureProxyResponseApi(codecFactory);
        EzyPackage pack = mock(EzyPackage.class);
        when(pack.getTransportType()).thenReturn(EzyConnectionType.SOCKET);

        // when
        api.response(pack);

        // then
        verify(codecFactory, times(1)).newEncoder(EzyConnectionType.SOCKET);
        verify(codecFactory, times(1)).newEncoder(EzyConnectionType.WEBSOCKET);
        verifyNoMoreInteractions(codecFactory);

        verifyNoMoreInteractions(encoder);

        verify(pack, times(1)).isEncrypted();
        verify(pack, times(1)).getRecipients(EzyConnectionType.SOCKET);
        verifyNoMoreInteractions(pack);
    }
}
