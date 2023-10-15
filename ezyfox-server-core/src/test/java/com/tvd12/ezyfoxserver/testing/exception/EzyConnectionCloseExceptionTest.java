package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.exception.EzyConnectionCloseException;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyConnectionCloseExceptionTest {

    @Test
    public void createByMsgAndThrowableTest() {
        // given
        Throwable e = new Throwable("test");

        // when
        EzyConnectionCloseException instance = new EzyConnectionCloseException(
            "test",
            e
        );

        // then
        Asserts.assertEquals(instance.getMessage(), "test");
        Asserts.assertEquals(instance.getCause(), e);
    }
}
