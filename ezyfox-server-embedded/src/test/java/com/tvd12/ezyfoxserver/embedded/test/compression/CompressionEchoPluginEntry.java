package com.tvd12.ezyfoxserver.embedded.test.compression;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

public class CompressionEchoPluginEntry implements EzyPluginEntry {

    @Override
    public void config(EzyPluginContext ctx) {
        ctx.get(EzyPluginSetup.class).setRequestController((context, event) -> {
            EzyArray data = event.getData();
            boolean udp = data.get(0, boolean.class);
            boolean encrypted = data.get(1, boolean.class);
            EzyArray response = EzyEntityFactory.newArrayBuilder()
                .append(data.get(2, String.class))
                .build();
            context.send(
                response,
                event.getSession(),
                encrypted,
                udp ? EzyTransportType.UDP : EzyTransportType.TCP
            );
        });
    }

    public static class Loader extends EzyAbstractPluginEntryLoader {

        @Override
        public EzyPluginEntry load() {
            return new CompressionEchoPluginEntry();
        }
    }
}
