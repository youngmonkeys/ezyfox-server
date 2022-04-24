package com.tvd12.ezyfoxserver.request;

public class EzySimpleAccessAppRequest
    extends EzySimpleRequest<EzyAccessAppParams>
    implements EzyAccessAppRequest {
    private static final long serialVersionUID = 8501365918570013140L;

    @Override
    protected EzyAccessAppParams newParams() {
        return new EzySimpleAccessAppParams();
    }

}
