package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.constant.EzyAccessAppError;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

public class EzyAccessAppExceptionTest {

    @Test
    public void test() {
        // given
        EzyAccessAppException sut =
            new EzyAccessAppException("hello", EzyAccessAppError.MAXIMUM_USER);

        // when
        // then
        Asserts.assertEquals(sut.getError(), EzyAccessAppError.MAXIMUM_USER);
    }
}
