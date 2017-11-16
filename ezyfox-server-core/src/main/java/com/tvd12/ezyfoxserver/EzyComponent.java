package com.tvd12.ezyfoxserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlersFetcher;
import com.tvd12.ezyfoxserver.util.EzyListExceptionHandlers;

import lombok.Getter;

@Getter
public class EzyComponent implements EzyExceptionHandlersFetcher {

    @JsonIgnore
    protected EzyExceptionHandlers exceptionHandlers = new EzyListExceptionHandlers();
    
}
