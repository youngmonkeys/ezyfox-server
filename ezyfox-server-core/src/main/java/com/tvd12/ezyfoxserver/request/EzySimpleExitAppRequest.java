package com.tvd12.ezyfoxserver.request;

public class EzySimpleExitAppRequest
    extends EzySimpleRequest<EzyExitAppParams>
    implements EzyExitAppRequest {
    private static final long serialVersionUID = 2479340122873879033L;

    @Override
    protected EzyExitAppParams newParams() {
        return new EzySimpleExitAppParams();
    }
}
