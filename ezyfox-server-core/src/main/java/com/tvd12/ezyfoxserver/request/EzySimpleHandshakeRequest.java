package com.tvd12.ezyfoxserver.request;

public class EzySimpleHandshakeRequest
    extends EzySimpleRequest<EzyHandshakeParams>
    implements EzyHandShakeRequest {
    private static final long serialVersionUID = -7925555874743788278L;

    @Override
    protected EzyHandshakeParams newParams() {
        return new EzySimpleHandShakeParams();
    }

}
