package com.tvd12.ezyfoxserver.embedded.test;

import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

public class TestPluginEntryLoader extends EzyAbstractPluginEntryLoader {

    @Override
    public EzyPluginEntry load() throws Exception {
        return new TestPluginEntry();
    }

}
