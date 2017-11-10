package com.tvd12.ezyfoxserver.testing.request;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.request.EzyRequestAppParams;
import com.tvd12.ezyfoxserver.request.impl.EzySimpleRequestAppParams;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzySimpleRequestAppRequestTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyRequestAppParams request = EzySimpleRequestAppParams.builder()
                .appId(1)
                .data(newArrayBuilder().build())
                .build();
        request.toString();
        
        assert request.getAppId() == 1;
        assert request.getData() instanceof EzyArray;
                
    }
    
}
