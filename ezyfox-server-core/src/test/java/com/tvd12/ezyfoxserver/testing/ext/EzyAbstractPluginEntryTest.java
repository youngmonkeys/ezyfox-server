package com.tvd12.ezyfoxserver.testing.ext;

import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntry;
import org.testng.annotations.Test;

public class EzyAbstractPluginEntryTest {

    @Test
    public void newTest() {
        EzyAbstractPluginEntry entry = new EzyAbstractPluginEntry() {};
        entry.destroy();
    }
}
