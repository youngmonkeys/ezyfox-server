package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfoxserver.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import lombok.Setter;

public abstract class EzySocketEventLoopHandler 
		extends EzyLoggable
		implements EzyThreadPoolSizeAware, EzyStartable, EzyDestroyable {

    @Setter
    protected int threadPoolSize;
    protected EzySocketEventLoop eventLoop;
    @Setter
    protected EzySocketEventHandler eventHandler;
    
	protected abstract String getThreadName();

	@Override
	public void start() throws Exception {
	    this.eventLoop = newEventLoop();
	    this.eventLoop.start();
	}
	
	private EzySocketEventLoop newEventLoop() {
	    return new EzySocketEventLoop() {
            
	        @Override
            protected void eventLoop() {
	            getLogger().info("{} event loop has started", currentThreadName());
                while(active) {
                    eventHandler.handleEvent();
                }
                getLogger().info("{} event loop has stopped", currentThreadName());
            }
	        
	        @Override
            protected String threadName() {
                return getThreadName();
            }
	        
            @Override
            protected int threadPoolSize() {
                return threadPoolSize;
            }
            
            private String currentThreadName() {
                return Thread.currentThread().getName();
            }
            
        };
	}
	
	@Override
	public void destroy() {
		if(eventLoop != null)
		    processWithLogException(eventLoop::destroy);
		if(eventHandler != null)
		    processWithLogException(eventHandler::destroy);
	}
	
}
