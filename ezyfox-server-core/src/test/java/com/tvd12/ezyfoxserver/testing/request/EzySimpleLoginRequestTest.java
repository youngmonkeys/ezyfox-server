package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.request.EzyLoginParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleLoginParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleLoginRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyLoginParams request = EzySimpleLoginParams.builder()
                .data(newArrayBuilder().build())
                .password("")
                .username("")
                .build();
        request.toString();
    }
    
}
