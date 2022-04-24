package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.event.EzySimpleStreamingEvent;
import org.testng.annotations.Test;

public class EzySimpleStreamingEventTest {

    @Test
    public void test() {
        EzySimpleStreamingEvent event = new EzySimpleStreamingEvent(null, null, null);
        assert event.getBytes() == null;
    }

}
