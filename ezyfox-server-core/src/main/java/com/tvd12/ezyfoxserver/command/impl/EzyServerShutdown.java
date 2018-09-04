package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

public class EzyServerShutdown 
        extends EzyAbstractCommand
        implements EzyShutdown {

    private final EzyServerContext context;
    
    public EzyServerShutdown(EzyServerContext context) {
        this.context = context;
    }
    
    @Override
    public void execute() {
        ((EzyDestroyable)context).destroy();
    }
    
}
