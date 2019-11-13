package com.tvd12.ezyfoxserver.testing.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyAccessAppError;
import com.tvd12.ezyfoxserver.exception.EzyAccessAppException;

public class EzyAccessAppExceptionTest {

    @Test
    public void test() {
        new EzyAccessAppException("hello", EzyAccessAppError.MAXIMUM_USER);
    }
    
}
