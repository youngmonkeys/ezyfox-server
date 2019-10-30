package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum EzyRequestAppError implements EzyIRequestAppError {

    HAS_NOT_ACCESSED(1, "user hasn't accessed app yet");
    
    @Getter
    private final int id;
    
    @Getter
    private final String message;
    
    private EzyRequestAppError(int id, String message) {
        this.id = id;
        this.message = message;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
    public static EzyRequestAppError valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }

}
