package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import lombok.Getter;

public enum EzyMaxRequestPerSecondAction implements EzyConstant {

    DROP_REQUEST(1),
    DISCONNECT_SESSION(2);

    @Getter
    private final int id;

    EzyMaxRequestPerSecondAction(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return toString();
    }
}
