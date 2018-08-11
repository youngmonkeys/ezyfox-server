package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum EzyAccessAppError implements EzyIAccessAppError {

    HAS_JOINED(0, "user has joined app"),
    MAXIMUM_USER(6, "app has maximum users");
    
    @Getter
    private final int id;
    
    @Getter
    private final String message;
    
    private EzyAccessAppError(int id, String message) {
        this.id = id;
        this.message = message;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
    public static EzyAccessAppError valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }
    
}
