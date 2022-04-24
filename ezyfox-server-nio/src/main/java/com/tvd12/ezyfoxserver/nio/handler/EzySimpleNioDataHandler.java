package com.tvd12.ezyfoxserver.nio.handler;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.handler.EzySimpleDataHandler;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;

public class EzySimpleNioDataHandler
        extends EzySimpleDataHandler<EzyNioSession>
        implements EzyNioDataHandler {

    public EzySimpleNioDataHandler(EzyServerContext ctx, EzyNioSession session) {
        super(ctx, session);
    }

    @Override
    public void channelRead(EzyCommand cmd, EzyArray msg)  throws Exception {
        dataReceived(cmd, msg);
    }
    
}
