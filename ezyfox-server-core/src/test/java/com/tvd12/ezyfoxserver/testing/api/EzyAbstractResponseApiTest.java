package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;
import com.tvd12.test.reflect.MethodUtil;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class EzyAbstractResponseApiTest {

    @Test
    public void secureResponseNoRecipients() {
        // given
        EzyPackage packet = mock(EzyPackage.class);
        when(packet.getRecipients(EzyConnectionType.SOCKET)).thenReturn(Collections.emptyList());

        InternalResponseApi sut = new InternalResponseApi();

        // when
        MethodInvoker.create()
            .object(sut)
            .method("secureResponse")
            .param(EzyPackage.class, packet)
            .param(boolean.class, true)
            .invoke();

        // then
        verify(packet, times(1)).getRecipients(EzyConnectionType.SOCKET);
    }

    @Test
    public void dataToMessageContentTest() {
        // given
        EzyArray data = EzyEntityFactory.EMPTY_ARRAY;

        InternalResponseApi sut = new InternalResponseApi();

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodInvoker.create()
                .object(sut)
                .method("dataToMessageContent")
                .param(EzyArray.class, data)
                .invoke()
        );

        // then
        Asserts.assertEquals(UnsupportedOperationException.class, e.getCause().getCause().getClass());
    }

    @Test
    public void encryptMessageContentTest() {
        // given
        byte[] messageContent = new byte[0];
        byte[] encryptionKey = new byte[0];

        InternalResponseApi sut = new InternalResponseApi();

        // when
        Throwable e = Asserts.assertThrows(() ->
            MethodUtil.invokeMethod("encryptMessageContent", sut, messageContent, encryptionKey));

        // then
        Asserts.assertEquals(UnsupportedOperationException.class, e.getCause().getCause().getClass());
    }

    private static class InternalResponseApi extends EzyAbstractResponseApi {

        @Override
        protected EzyConstant getConnectionType() {
            return EzyConnectionType.SOCKET;
        }

        @Override
        protected Object encodeData(EzyArray data) {
            return null;
        }
    }
}
