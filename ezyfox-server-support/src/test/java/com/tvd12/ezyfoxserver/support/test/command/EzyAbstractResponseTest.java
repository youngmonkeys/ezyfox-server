package com.tvd12.ezyfoxserver.support.test.command;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.command.EzyResponse;
import com.tvd12.ezyfoxserver.support.command.EzyAbstractResponse;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class EzyAbstractResponseTest {

    private EzyResponse response;

    @Test
    public void test() {
        // given
        response = mock(EzyResponse.class);
        Sut sut = new Sut();

        // when
        sut.encrypted();
        sut.encrypted(true);

        // then
        verify(response, times(1)).encrypted();
        verify(response, times(1)).encrypted(true);
    }

    @SuppressWarnings("rawtypes")
    public class Sut extends EzyAbstractResponse {

        public Sut() {
            super(null, null);
        }

        @Override
        protected EzyData getResponseData() {
            return null;
        }

        @Override
        protected EzyResponse newResponse() {
            return EzyAbstractResponseTest.this.response;
        }

    }
}
