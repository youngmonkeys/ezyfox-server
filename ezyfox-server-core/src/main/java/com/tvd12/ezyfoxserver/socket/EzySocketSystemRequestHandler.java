package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

public class EzySocketSystemRequestHandler extends EzySocketRequestHandler {

    @Override
    protected EzyRequestQueue getRequestQueue(EzySession session) {
        return session.getSystemRequestQueue();
    }

    @Override
    protected String getRequestType() {
        return "system";
    }

}
