package com.tvd12.ezyfoxserver.constant;

import lombok.Getter;

public enum EzyAccessAppError implements EzyIAccessAppError {

    MAXIMUM_USER(6, "app has maximum users");

    @Getter
    private final int id;

    @Getter
    private final String message;

    EzyAccessAppError(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String getName() {
        return toString();
    }
}
