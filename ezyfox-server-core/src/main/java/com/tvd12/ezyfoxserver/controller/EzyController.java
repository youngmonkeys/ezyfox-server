package com.tvd12.ezyfoxserver.controller;

public interface EzyController<C, R> {

    void handle(C ctx, R request);

}
