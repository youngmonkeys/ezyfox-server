package com.tvd12.ezyfoxserver.testing.ext;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

public class EzyAbstractPluginEntryLoaderTest {

    @Test
    public void newTest() {
        new EzyAbstractPluginEntryLoader() {
            @Override
            public EzyPluginEntry load() throws Exception {
                return null;
            }
        };
    }
}
