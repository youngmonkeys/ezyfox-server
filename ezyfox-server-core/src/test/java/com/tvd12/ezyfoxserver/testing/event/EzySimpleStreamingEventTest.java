package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.event.EzySimpleStreamingEvent;

public class EzySimpleStreamingEventTest {

    @Test
    public void test() {
        EzySimpleStreamingEvent event = new EzySimpleStreamingEvent(null, null, null);
        assert event.getBytes() == null;
    }
    
}
