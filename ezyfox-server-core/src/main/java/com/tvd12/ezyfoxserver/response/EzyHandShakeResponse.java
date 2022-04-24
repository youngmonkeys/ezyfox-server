package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyHandShakeResponse
    extends EzySimpleParamsResponse<EzyHandShakeParams> {
    private static final long serialVersionUID = -464917427945070426L;

    public EzyHandShakeResponse(EzyHandShakeParams params) {
        super(EzyCommand.HANDSHAKE, params);
    }

}
