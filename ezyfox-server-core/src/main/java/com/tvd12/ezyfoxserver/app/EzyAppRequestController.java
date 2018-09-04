package com.tvd12.ezyfoxserver.app;

import com.tvd12.ezyfoxserver.controller.EzyAppEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestAppEvent;

public interface EzyAppRequestController extends EzyAppEventController<EzyUserRequestAppEvent> {
    EzyAppRequestController DEFAULT = (context, event) -> {};
}
