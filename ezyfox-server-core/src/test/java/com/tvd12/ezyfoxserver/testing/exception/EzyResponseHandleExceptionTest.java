package com.tvd12.ezyfoxserver.testing.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyResponseHandleException;
import com.tvd12.test.base.BaseTest;

public class EzyResponseHandleExceptionTest extends BaseTest {

    @Test(expectedExceptions = {EzyResponseHandleException.class})
    public void test() {
        throw new EzyResponseHandleException("msg", new Exception());
    }
    
}
