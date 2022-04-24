package com.tvd12.ezyfoxserver.testing.event;

import com.tvd12.ezyfoxserver.event.EzySimpleServerReadyEvent;
import com.tvd12.ezyfoxserver.testing.BaseCoreTest;
import org.testng.annotations.Test;

public class EzyServerReadyEventImplTest extends BaseCoreTest {

    @Test
    public void test() {
        new EzySimpleServerReadyEvent();
    }
}
