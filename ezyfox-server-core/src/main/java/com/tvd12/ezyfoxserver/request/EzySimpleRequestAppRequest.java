package com.tvd12.ezyfoxserver.request;

public class EzySimpleRequestAppRequest
    extends EzySimpleRequest<EzyRequestAppParams>
    implements EzyRequestAppRequest {
    private static final long serialVersionUID = 5909672063016351418L;

    @Override
    protected EzyRequestAppParams newParams() {
        return new EzySimpleRequestAppParams();
    }

}
