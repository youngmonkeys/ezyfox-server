package com.tvd12.ezyfoxserver.socket;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfox.util.EzyStartable;

import lombok.Setter;

public abstract class EzySocketEventLoopHandler 
		extends EzyLoggable
		implements EzyThreadPoolSizeAware, EzyStartable, EzyDestroyable {

    @Setter
    protected int threadPoolSize;
    protected EzySocketEventLoop eventLoop;
    
	@Override
	public void start() throws Exception {
	    this.eventLoop = newEventLoop0();
	    this.eventLoop.start();
	}
	
	private EzySimpleSocketEventLoop newEventLoop0() {
	    EzySimpleSocketEventLoop eventLoop = newEventLoop();
	    eventLoop.setThreadName(getThreadName());
	    eventLoop.setThreadPoolSize(threadPoolSize);
	    return eventLoop;
	}
	
	protected abstract String getThreadName();
	
	protected abstract EzySimpleSocketEventLoop newEventLoop();
	
	@Override
	public void destroy() {
		if(eventLoop != null)
		    processWithLogException(eventLoop::destroy);
	}
	
}
