package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzySimpleSocketDisconnection implements EzySocketDisconnection {

    private EzySession session;
    private EzyConstant disconnectReason;

    public EzySimpleSocketDisconnection(EzySession session) {
        this(session, EzyDisconnectReason.UNKNOWN);
    }

    @Override
    public void release() {
        this.session = null;
        this.disconnectReason = null;
    }

    @Override
    public String toString() {
        return "session: " + session +
            ", disconnection reason: " + disconnectReason;
    }
}
