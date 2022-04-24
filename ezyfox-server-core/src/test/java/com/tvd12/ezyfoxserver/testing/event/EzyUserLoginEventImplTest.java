package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.util.EzyMapBuilder;
import com.tvd12.ezyfoxserver.event.EzySimpleUserLoginEvent;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyUserLoginEventImplTest extends BaseCoreTest {

    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        EzyArray data = newArrayBuilder().build();
        data.add("123.abc");
        EzyObject output2 = newObjectBuilder().append("2", "b").build();
        EzyUserLoginEvent event = new EzySimpleUserLoginEvent(null, "zone", "dungtv", "123", data);
        assert event.getData() == data;
        assert event.getUsername().equals("dungtv");
        assert event.getPassword().equals("123");
        assert event.getOutput() == null;

        event.setUsername("new login name");
        event.setPassword("new password");
        event.setOutput(output2);

        assert event.getUsername().equals("new login name");
        assert event.getPassword().equals("new password");
        assert event.getOutput() == output2;

        event.setUserProperty("id", 1);
        event.setUserProperties(EzyMapBuilder.mapBuilder().build());
        assert event.getUserProperties().size() == 1;
        event.setStreamingEnable(true);
        assert event.isStreamingEnable();
        assert event.getZoneName().equals("zone");

        event = new EzySimpleUserLoginEvent(null, null, null, null, null);

        assert event.getUsername().equals("");
        assert event.getPassword().equals("");
    }
}
