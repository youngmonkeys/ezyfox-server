package com.tvd12.ezyfoxserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfox.util.EzyListExceptionHandlers;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzyComponent implements EzyExceptionHandlersFetcher, EzyDestroyable {

    @JsonIgnore
    @Setter
    protected EzyEventControllers eventControllers;
    
    @JsonIgnore
    protected final EzyExceptionHandlers exceptionHandlers 
            = new EzyListExceptionHandlers();
    
    @Override
    public void destroy() {
        this.eventControllers = null;
    }
    
}
