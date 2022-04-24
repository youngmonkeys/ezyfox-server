package com.tvd12.ezyfoxserver.handler;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.request.EzyRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleStreamingRequest;
import com.tvd12.ezyfoxserver.request.EzyStreamingRequest;

public abstract class EzyUserDataHandler<S extends EzySession>
    extends EzySessionDataHandler<S> {

    protected final EzyRequestFactory requestFactory;

    public EzyUserDataHandler(EzyServerContext ctx, S session) {
        super(ctx, session);
        this.requestFactory = newRequestFactory();
    }

    protected EzyRequestFactory newRequestFactory() {
        return new EzySimpleRequestFactory();
    }

    protected void checkToUnmapUser(EzyConstant reason) {
        if (user != null) {
            userManager.unmapSessionUser(session, reason);
        }
    }

    @SuppressWarnings({"rawtypes"})
    protected EzyRequest newRequest(EzyConstant cmd, EzyArray data) {
        EzySimpleRequest request = requestFactory.newRequest(cmd);
        request.setSession(session);
        request.setUser(user);
        request.deserializeParams(data);
        return request;
    }

    protected EzyStreamingRequest newStreamingRequest(byte[] bytes) {
        EzySimpleStreamingRequest request = new EzySimpleStreamingRequest();
        request.setUser(user);
        request.setSession(session);
        request.setBytes(bytes);
        return request;
    }
}
