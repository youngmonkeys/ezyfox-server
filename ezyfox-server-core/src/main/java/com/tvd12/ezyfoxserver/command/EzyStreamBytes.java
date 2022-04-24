package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.entity.EzySession;

import java.util.Collection;

public interface EzyStreamBytes {

    void execute(
        byte[] bytes,
        EzySession recipient,
        EzyTransportType transportType
    );

    void execute(
        byte[] bytes,
        Collection<EzySession> recipients,
        EzyTransportType transportType
    );

    default void execute(byte[] bytes, EzySession recipient) {
        execute(bytes, recipient, EzyTransportType.TCP);
    }

    default void execute(byte[] bytes, Collection<EzySession> recipients) {
        execute(bytes, recipients, EzyTransportType.TCP);
    }
}
