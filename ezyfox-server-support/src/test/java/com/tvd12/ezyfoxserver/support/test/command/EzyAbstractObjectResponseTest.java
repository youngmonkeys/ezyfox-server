package com.tvd12.ezyfoxserver.support.test.command;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.collect.Sets;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.support.command.EzyAbstractObjectResponse;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.FieldUtil;
import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAbstractObjectResponseTest {

    @Test
    public void exclude2Times() {
        // given
        EzyContext context = mock(EzyContext.class);
        EzyMarshaller marshaller = mock(EzyMarshaller.class);

        String excludeKey1 = RandomUtil.randomShortAlphabetString();
        String excludeKey2 = RandomUtil.randomShortAlphabetString();

        // when
        EzyObjectResponse sut = new InternalObjectResponse(
            context,
            marshaller
        )
            .exclude(excludeKey1)
            .exclude(excludeKey2);

        // then
        Asserts.assertEquals(
            FieldUtil.getFieldValue(sut, "excludeParamKeys"),
            Sets.newHashSet(excludeKey1, excludeKey2)
        );
    }

    @Test
    public void udpOrTcpTransportTest() {
        // given
        EzyContext context = mock(EzyContext.class);
        EzyMarshaller marshaller = mock(EzyMarshaller.class);

        // when
        EzyObjectResponse sut = new InternalObjectResponse(
            context,
            marshaller
        )
            .udpOrTcpTransport();

        // then
        EzyResponse response = FieldUtil.getFieldValue(
            sut,
            "response"
        );
        verify(response, times(1)).transportType(
            EzyTransportType.UDP_OR_TCP
        );
    }

    private static class InternalObjectResponse
        extends EzyAbstractObjectResponse {

        public InternalObjectResponse(
            EzyContext context,
            EzyMarshaller marshaller
        ) {
            super(context, marshaller);
        }

        @Override
        protected EzyResponse newResponse() {
            if (response == null) {
                response = mock(EzyResponse.class);
            }
            return response;
        }
    }
}
