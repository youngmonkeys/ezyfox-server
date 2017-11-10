package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;
import com.tvd12.ezyfoxserver.event.impl.EzyUserLoginEventImpl;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyUserLoginEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyArray data = newArrayBuilder().build();
        data.add("123.abc");
        EzyObject output = newObjectBuilder().append("1", "a").build();
        EzyObject output2 = newObjectBuilder().append("2", "b").build();
        EzyUserLoginEvent event = EzyUserLoginEventImpl.builder()
                .data(data)
                .output(output)
                .username("dungtv")
                .password("123")
                .build();
        assert event.getData() == data;
        assert event.getUsername().equals("dungtv");
        assert event.getPassword().equals("123");
        assert event.getOutput() == output;
        
        event.setUsername("new login name");
        event.setPassword("new password");
        event.setOutput(output2);
        
        assert event.getUsername().equals("new login name");
        assert event.getPassword().equals("new password");
        assert event.getOutput() == output2;
    }
    
}
