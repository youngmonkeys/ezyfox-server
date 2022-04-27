package com.tvd12.ezyfoxserver.support.test.exception;

import com.tvd12.ezyfoxserver.support.exception.EzyUserRequestException;
import org.testng.annotations.Test;

public class EzyUserRequestExceptionTest {

    @Test
    public void test() {
        EzyUserRequestException ex = new EzyUserRequestException("cmd", "data", new Exception());
        assert ex.getCommand().equals("cmd");
        assert ex.getData().equals("data");
    }
}
