package com.tvd12.ezyfoxserver.testing.controller;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzySimpleStreamingController;
import com.tvd12.ezyfoxserver.request.EzySimpleStreamingRequest;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

public class EzySimpleStreamingControllerTest extends BaseTest {
    
    @Test
    public void test() {
        EzySimpleStreamingController controller = new EzySimpleStreamingController();
        EzyZoneContext zoneContext = mock(EzyZoneContext.class);
        EzySimpleStreamingRequest request = new EzySimpleStreamingRequest();
        controller.handle(zoneContext, request);
    }

}
