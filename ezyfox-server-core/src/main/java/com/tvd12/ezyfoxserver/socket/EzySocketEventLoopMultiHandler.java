package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import lombok.Setter;

public abstract class EzySocketEventLoopMultiHandler extends EzySocketEventLoopHandler {

    @Setter
    protected Supplier<EzySocketEventHandler> eventHandlerSupplier;
    protected List<EzySocketEventHandler> eventHandlers = new ArrayList<>();

	@Override
	protected EzySimpleSocketEventLoop newEventLoop() {
	    return new EzySimpleSocketEventLoop() {
            
	        @Override
            protected void eventLoop0() {
	            EzySocketEventHandler eventHandler = eventHandlerSupplier.get();
	            eventHandlers.add(eventHandler);
                while(active) {
                    eventHandler.handleEvent();
                }
            }
            
        };
	}
	
	@Override
	public void destroy() {
		if(eventLoop != null)
		    processWithLogException(eventLoop::destroy);
		for(EzySocketEventHandler eventHandler : eventHandlers)
		    processWithLogException(eventHandler::destroy);
	}
	
}
