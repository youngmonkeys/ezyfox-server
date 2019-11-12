package com.tvd12.ezyfoxserver.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyChildComponent;
import com.tvd12.ezyfoxserver.ext.EzyEntry;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.test.base.BaseTest;
import static org.mockito.Mockito.*;

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
