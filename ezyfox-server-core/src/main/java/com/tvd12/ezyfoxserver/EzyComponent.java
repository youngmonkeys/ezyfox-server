package com.tvd12.ezyfoxserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfox.util.EzyExceptionHandlers;
import com.tvd12.ezyfox.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfox.util.EzyListExceptionHandlers;

import lombok.Getter;

@Getter
public class EzyComponent implements EzyExceptionHandlersFetcher {

    @JsonIgnore
    protected final EzyExceptionHandlers exceptionHandlers 
            = new EzyListExceptionHandlers();
    
}
