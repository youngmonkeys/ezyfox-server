package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzySessionError;

public class EzySessionErrorTest {

    @Test
    public void test() {
        System.out.println(EzySessionError.MAX_REQUEST_PER_SECOND.getName());
    }

}
