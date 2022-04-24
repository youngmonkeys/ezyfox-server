package com.tvd12.ezyfoxserver.support.test.exception;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.support.exception.EzyUserRequestException;

public class EzyUserRequestExceptionTest {

    @Test
    public void test() {
        EzyUserRequestException ex = new EzyUserRequestException("cmd", "data", new Exception());
        assert ex.getCommand().equals("cmd");
        assert ex.getData().equals("data");
    }

}
