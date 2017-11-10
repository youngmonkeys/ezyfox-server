package com.tvd12.ezyfoxserver.testing.event;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.event.impl.EzyServerReadyEventImpl;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;

public class EzyServerReadyEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        EzyServerReadyEventImpl.builder()
                .build();
    }
    
}
