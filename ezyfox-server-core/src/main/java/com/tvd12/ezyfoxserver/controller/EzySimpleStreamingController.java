package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.event.EzySimpleStreamingEvent;
import com.tvd12.ezyfoxserver.event.EzyStreamingEvent;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;

public class EzySimpleStreamingController
    implements EzyStreamingController {

    @Override
    public void handle(EzyZoneContext ctx, EzyStreamingRequest request) {
        EzyStreamingEvent event = new EzySimpleStreamingEvent(
            request.getUser(),
            request.getSession(),
            request.getBytes());
        ctx.handleEvent(EzyEventType.STREAMING, event);
    }
}
