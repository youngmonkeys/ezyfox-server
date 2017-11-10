package com.tvd12.ezyfoxserver.testing.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.exception.EzyRequestHandleException;
import com.tvd12.test.base.BaseTest;

public class EzyRequestHandleExceptionTest extends BaseTest {

    @Test(expectedExceptions = {EzyRequestHandleException.class})
    public void test() {
        throw new EzyRequestHandleException("msg", new Exception());
    }
    
}
