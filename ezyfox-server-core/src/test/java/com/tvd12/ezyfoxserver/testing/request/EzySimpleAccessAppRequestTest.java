package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.request.EzyAccessAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleAccessAppParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleAccessAppRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyAccessAppParams request = EzySimpleAccessAppParams.builder()
                .appName("chat")
                .build();
        request.toString();
    }
    
}
