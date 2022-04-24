package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.exception.EzyResponseHandleException;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyResponseHandleExceptionTest extends BaseTest {

    @Test(expectedExceptions = {EzyResponseHandleException.class})
    public void test() {
        throw new EzyResponseHandleException("msg", new Exception());
    }
}
