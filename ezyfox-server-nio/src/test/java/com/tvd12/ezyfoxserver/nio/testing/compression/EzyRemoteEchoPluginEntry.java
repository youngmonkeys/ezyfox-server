package com.tvd12.ezyfoxserver.nio.testing.compression;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.command.EzyPluginSetup;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

import java.util.Random;

public class EzyRemoteEchoPluginEntry implements EzyPluginEntry {

    public static final String PLUGIN_NAME = "echo";
    public static final String COMMAND = "echo";

    private final Random random = new Random();

    @Override
    public void config(EzyPluginContext ctx) {
        ctx.get(EzyPluginSetup.class).setRequestController((context, event) -> {
            EzyArray data = event.getData();
            String cmd = data.get(0, String.class);
            EzyObject params = data.get(1, EzyObject.class);
            String upload = params.get("upload", String.class, "");
            int textSize = params.get("textSize", int.class, 0);
            int randomSize = params.get("randomSize", int.class, 0);
            boolean encrypted = params.get("encrypted", boolean.class, false);
            EzyObject response = EzyEntityFactory.newObjectBuilder()
                .append("uploadLength", upload.length())
                .append("uploadHash", upload.hashCode())
                .append("text", newText(textSize))
                .append("textHash", newText(textSize).hashCode())
                .append("random", newRandomBytes(randomSize))
                .build();
            context.send(
                EzyEntityFactory.newArrayBuilder()
                    .append(cmd)
                    .append(response)
                    .build(),
                event.getSession(),
                encrypted
            );
        });
    }

    public static String newText(int size) {
        StringBuilder builder = new StringBuilder(size);
        int index = 0;
        while (builder.length() < size) {
            builder.append("ezyfox compression line ").append(index++).append(";");
        }
        builder.setLength(size);
        return builder.toString();
    }

    private byte[] newRandomBytes(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return bytes;
    }

    @Override
    public void start() {}

    @Override
    public void destroy() {}

    public static class Loader extends EzyAbstractPluginEntryLoader {

        @Override
        public EzyPluginEntry load() {
            return new EzyRemoteEchoPluginEntry();
        }
    }
}
