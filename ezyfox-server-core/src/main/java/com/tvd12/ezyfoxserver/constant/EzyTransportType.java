package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import lombok.Getter;

public enum EzyTransportType implements EzyConstant {

    TCP(1),
    UDP(2),
    UDP_OR_TCP(3);

    @Getter
    private final int id;

    EzyTransportType(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return toString();
    }
}
