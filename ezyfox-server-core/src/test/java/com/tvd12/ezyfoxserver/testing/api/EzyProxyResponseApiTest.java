package com.tvd12.ezyfoxserver.testing.api;

import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.codec.EzyObjectToStringEncoder;
import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.api.EzyProxyResponseApi;
import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyImmediateDeliver;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyProxyResponseApiTest extends BaseTest {

    @Test
    public void test() throws Exception {
        EzyCodecFactory codecFactory = mock(EzyCodecFactory.class);
        EzyProxyResponseApi api = new EzyProxyResponseApi(codecFactory);
        EzyPackage pack = mock(EzyPackage.class);
        api.response(pack);
        EzyObjectToByteEncoder byteEncoder = mock(EzyObjectToByteEncoder.class);
        EzyObjectToStringEncoder stringEncoder = mock(EzyObjectToStringEncoder.class);
        when(codecFactory.newEncoder(EzyConnectionType.SOCKET)).thenReturn(byteEncoder);
        when(codecFactory.newEncoder(EzyConnectionType.WEBSOCKET)).thenReturn(stringEncoder);
        api = new EzyProxyResponseApi(codecFactory);
        api.response(pack);
        api.response(pack, true);

        EzyImmediateDeliver immediateDeliver = mock(EzyImmediateDeliver.class);
        EzyAbstractSession session = spy(EzyAbstractSession.class);
        session.setImmediateDeliver(immediateDeliver);
        when(pack.getRecipients(any(EzyConstant.class))).thenReturn(Lists.newArrayList(session));

        api.response(pack);
        api.response(pack, true);
    }

}
