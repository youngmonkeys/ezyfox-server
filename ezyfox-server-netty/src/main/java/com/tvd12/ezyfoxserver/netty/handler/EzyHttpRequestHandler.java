package com.tvd12.ezyfoxserver.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class EzyHttpRequestHandler
		extends SimpleChannelInboundHandler<FullHttpRequest> {

	protected final String wsUri;

    public EzyHttpRequestHandler(String wsUri) {
        this.wsUri = wsUri;
    }

	@Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
        throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri()))
            ctx.fireChannelRead(request.retain());
    }

}
