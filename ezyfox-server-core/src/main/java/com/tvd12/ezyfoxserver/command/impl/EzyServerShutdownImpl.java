package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.command.EzyAbstractCommand;
import com.tvd12.ezyfoxserver.command.EzyShutdown;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

public class EzyServerShutdownImpl
    extends EzyAbstractCommand
    implements EzyShutdown {

    private final EzyServerContext context;

    public EzyServerShutdownImpl(EzyServerContext context) {
        this.context = context;
    }

    @Override
    public void execute() {
        ((EzyDestroyable) context).destroy();
    }

}
