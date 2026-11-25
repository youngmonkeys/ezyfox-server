package com.tvd12.ezyfoxserver.embedded.test.streaming;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.event.EzyStreamingEvent;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StreamingRecorderPluginEntry implements EzyPluginEntry {

    private static final BlockingQueue<EzyStreamingEvent> EVENTS =
        new LinkedBlockingQueue<>();

    public static EzyStreamingEvent pollEvent(
        long timeoutMillis
    ) throws InterruptedException {
        return EVENTS.poll(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    public static void clearEvents() {
        EVENTS.clear();
    }

    @Override
    public void config(EzyPluginContext ctx) {
        EzyEventController<EzyZoneContext, EzyStreamingEvent> controller =
            (context, event) -> EVENTS.add(event);
        ctx.getParent()
            .getZone()
            .getEventControllers()
            .addController(EzyEventType.STREAMING, controller);
    }

    public static class Loader extends EzyAbstractPluginEntryLoader {

        @Override
        public EzyPluginEntry load() {
            return new StreamingRecorderPluginEntry();
        }
    }
}
