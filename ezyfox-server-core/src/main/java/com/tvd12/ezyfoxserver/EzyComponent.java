package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfox.util.EzyListExceptionHandlers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzyComponent implements EzyExceptionHandlersFetcher, EzyDestroyable {

    @Setter
    protected EzyEventControllers eventControllers;
    
    protected final EzyExceptionHandlers exceptionHandlers 
            = new EzyListExceptionHandlers();
    
    @Override
    public void destroy() {
        this.eventControllers = null;
    }
    
}
