package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyChildComponent;
import com.tvd12.ezyfoxserver.ext.EzyEntry;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class EzyChildComponentTest extends BaseTest {

    @Test
    public void test() {
        EzyChildComponent component = spy(EzyChildComponent.class);
        EzyEntry entry = mock(EzyEntry.class);
        component.setEntry(entry);
        assert component.getEntry() == entry;

        EzyEventControllers eventControllers = mock(EzyEventControllers.class);
        component.setEventControllers(eventControllers);
        assert component.getEventControllers() == eventControllers;

        component.destroy();
        component.destroy();
    }
}
