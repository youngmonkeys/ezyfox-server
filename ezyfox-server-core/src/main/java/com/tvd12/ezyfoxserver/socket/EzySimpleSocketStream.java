package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;

@Getter
public class EzySimpleSocketStream implements EzySocketStream {

    private byte[] bytes;
    private long timestamp;
    private EzySession session;

    public EzySimpleSocketStream(EzySession session, byte[] bytes) {
        this.bytes = bytes;
        this.session = session;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void release() {
        this.bytes = null;
        this.session = null;
    }
}
