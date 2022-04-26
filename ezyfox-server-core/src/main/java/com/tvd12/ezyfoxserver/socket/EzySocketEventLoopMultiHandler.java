package com.tvd12.ezyfoxserver.socket;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

public abstract class EzySocketEventLoopMultiHandler
    extends EzySocketEventLoopHandler {

    @Setter
    protected Supplier<EzySocketEventHandler> eventHandlerSupplier;
    protected final List<EzySocketEventHandler> eventHandlers
        = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected EzySimpleSocketEventLoop newEventLoop() {
        return new EzySimpleSocketEventLoop() {

            @Override
            protected void doEventLoop() {
                EzySocketEventHandler eventHandler =
                    eventHandlerSupplier.get();
                eventHandlers.add(eventHandler);
                while (active) {
                    eventHandler.handleEvent();
                }
            }
        };
    }

    @Override
    public void destroy() {
        super.destroy();
        eventHandlers.forEach(it ->
            processWithLogException(it::destroy)
        );
        eventHandlers.clear();
    }
}
