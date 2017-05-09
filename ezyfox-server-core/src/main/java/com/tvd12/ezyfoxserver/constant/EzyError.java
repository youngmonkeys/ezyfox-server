package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyError implements EzyIError {

    MAX_REQUEST_PER_SECOND(0, "max request per second");
    
    @Getter
    private final int id;
    
    @Getter
    private final String message;
    
    private EzyError(int id, String message) {
        this.id = id;
        this.message = message;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
    public static EzyError valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }
    
}
