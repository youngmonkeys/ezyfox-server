package com.tvd12.ezyfoxserver.socket;

import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzySocketEventLoopOneHandler extends EzySocketEventLoopHandler {

    @Setter
    protected EzySocketEventHandler eventHandler;

    @Override
    protected EzySimpleSocketEventLoop newEventLoop() {
        return new EzySimpleSocketEventLoop() {

            @Override
            protected void doEventLoop() {
                while (active) {
                    eventHandler.handleEvent();
                }
            }
        };
    }

    @Override
    public void destroy() {
        super.destroy();
        if (eventHandler != null) {
            processWithLogException(eventHandler::destroy);
        }
    }
}
