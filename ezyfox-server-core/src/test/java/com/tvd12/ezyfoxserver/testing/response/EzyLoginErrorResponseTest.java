package com.tvd12.ezyfoxserver.testing.response;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.response.EzyLoginErrorResponse;
import com.tvd12.test.base.BaseTest;

public class EzyLoginErrorResponseTest extends BaseTest {

    @Test
    public void test() {
        EzyLoginErrorResponse response = EzyLoginErrorResponse.builder()
                .code(10)
                .message("123")
                .build();
        EzyArray data = (EzyArray) response.getData();
        assert data.get(0).equals(new Integer(10));
        assert data.get(1).equals("123");
    }
    
}
