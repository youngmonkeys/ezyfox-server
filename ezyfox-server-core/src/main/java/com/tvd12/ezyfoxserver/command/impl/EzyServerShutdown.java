package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public class EzyServerShutdown 
        extends EzyAbstractCommand
        implements EzyShutdown {

    private EzyServerContext context;
    
    public EzyServerShutdown(EzyServerContext context) {
        this.context = context;
    }
    
    @Override
    public Boolean execute() {
        ((EzyDestroyable)context).destroy();
        return Boolean.TRUE;
    }
    
}
