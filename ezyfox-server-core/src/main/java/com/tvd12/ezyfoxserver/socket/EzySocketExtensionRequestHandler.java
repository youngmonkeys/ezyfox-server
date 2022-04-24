package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzySocketExtensionRequestHandler extends EzySocketRequestHandler {

    @Override
    protected EzyRequestQueue getRequestQueue(EzySession session) {
        return session.getExtensionRequestQueue();
    }

    @Override
    protected String getRequestType() {
        return "extension";
    }

}
