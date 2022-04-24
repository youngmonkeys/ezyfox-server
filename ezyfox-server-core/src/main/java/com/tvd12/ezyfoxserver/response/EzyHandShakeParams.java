package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyHandShakeParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = 6597013677969259046L;

    protected byte[] serverPublicKey;
    protected String reconnectToken;
    protected long sessionId;
    protected byte[] sessionKey;

    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
            .append(serverPublicKey)
            .append(reconnectToken)
            .append(sessionId)
            .append(sessionKey);
    }
}
