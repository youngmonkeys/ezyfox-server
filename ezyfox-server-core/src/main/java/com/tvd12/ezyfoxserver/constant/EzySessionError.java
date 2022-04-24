package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzySessionError implements EzyIError {

    MAX_REQUEST_PER_SECOND(0, "max request per second");

    @Getter
    private final int id;

    @Getter
    private final String message;

    EzySessionError(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getName() {
        return toString();
    }
}
