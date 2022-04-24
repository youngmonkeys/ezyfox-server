package com.tvd12.ezyfoxserver.testing.exception;

import com.tvd12.ezyfoxserver.exception.EzyMaxSessionException;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyMaxSessionExceptionTest extends BaseTest {

    @Test
    public void test() {
        EzyMaxSessionException exception = new EzyMaxSessionException(1, 2);
        System.out.print(exception.getMessage());
    }

}
