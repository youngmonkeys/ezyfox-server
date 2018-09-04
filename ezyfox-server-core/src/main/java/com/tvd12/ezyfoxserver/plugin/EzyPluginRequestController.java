package com.tvd12.ezyfoxserver.plugin;

import com.tvd12.ezyfoxserver.controller.EzyPluginEventController;
import com.tvd12.ezyfoxserver.event.EzyUserRequestPluginEvent;

public interface EzyPluginRequestController extends EzyPluginEventController<EzyUserRequestPluginEvent> {
    EzyPluginRequestController DEFAULT = (context, event) -> {};
}
