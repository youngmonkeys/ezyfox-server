package com.tvd12.ezyfoxserver.controller;

public interface EzyEventController<C, E> {

    void handle(C ctx, E event);
}
