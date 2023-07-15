package com.tvd12.ezyfoxserver.testing.ssl;

import org.testng.annotations.Test;

import javax.net.ssl.SSLEngine;

import static com.tvd12.ezyfoxserver.ssl.EzySslEngines.safeCloseOutbound;
import static org.mockito.Mockito.*;

public class EzySslEnginesTest {

    @Test
    public void safeCloseOutboundTest() {
        // given
        SSLEngine engine = mock(SSLEngine.class);

        // when
        safeCloseOutbound(engine);

        // then
        verify(engine, times(1)).closeOutbound();
    }

    @Test
    public void safeCloseOutboundCaseExceptionTest() {
        // given
        SSLEngine engine = mock(SSLEngine.class);
        RuntimeException error = new RuntimeException("test");
        doThrow(error).when(engine).closeOutbound();

        // when
        safeCloseOutbound(engine);

        // then
        verify(engine, times(1)).closeOutbound();
    }
}
