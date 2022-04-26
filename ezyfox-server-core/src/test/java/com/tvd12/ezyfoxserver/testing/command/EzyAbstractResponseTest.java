package com.tvd12.ezyfoxserver.testing.command;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyAbstractResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyZoneChildContext;
import com.tvd12.ezyfoxserver.wrapper.EzyUserManager;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;

public class EzyAbstractResponseTest {

    @Test
    public void encryptedTest() {
        // given
        EzyZoneChildContext context = mock(EzyZoneChildContext.class);
        InternalResponse sut = new InternalResponse(context);

        // when
        sut.encrypted();

        // then
        Asserts.assertTrue(FieldUtil.getFieldValue(sut, "encrypted"));
    }

    @Test
    public void encryptedSetTest() {
        // given
        EzyZoneChildContext context = mock(EzyZoneChildContext.class);
        InternalResponse sut = new InternalResponse(context);

        // when
        sut.encrypted(true);

        // then
        Asserts.assertTrue(FieldUtil.getFieldValue(sut, "encrypted"));
    }

    @SuppressWarnings("rawtypes")
    private static class InternalResponse extends EzyAbstractResponse {

        @SuppressWarnings("unchecked")
        public InternalResponse(EzyZoneChildContext context) {
            super(context);
        }

        @Override
        protected EzyUserManager getUserManager(EzyZoneChildContext context) {
            return null;
        }

        @Override
        protected void sendData(EzyData data, EzyTransportType transportType) {}
    }
}
