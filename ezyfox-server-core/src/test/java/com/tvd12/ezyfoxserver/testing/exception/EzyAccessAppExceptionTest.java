package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.constant.EzyAccessAppError;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;
import org.testng.annotations.Test;

public class EzyAccessAppExceptionTest {

    @Test
    public void test() {
        new EzyAccessAppException("hello", EzyAccessAppError.MAXIMUM_USER);
    }
}
