package com.tvd12.ezyfoxserver.testing.ext;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntry;

public class EzyAbstractPluginEntryTest {

    @Test
    public void newTest() {
        EzyAbstractPluginEntry entry = new EzyAbstractPluginEntry() {
        };
        entry.destroy();
    }
}
