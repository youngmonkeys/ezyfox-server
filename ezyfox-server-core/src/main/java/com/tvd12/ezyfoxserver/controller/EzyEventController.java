package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfox.constant.EzyConstant;

public interface EzyEventController<C, E> {

    void handle(C ctx, E event);

    default EzyConstant getEventType() {
        return null;
    }
}
