package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfoxserver.util.EzyEnums;

import lombok.Getter;

public enum EzyMaxRequestPerSecondAction implements EzyConstant {

    DROP_REQUEST(1),
    DISCONNECT_SESSION(2);
    
    @Getter
    private final int id;
    
    private EzyMaxRequestPerSecondAction(int id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
    public static EzyMaxRequestPerSecondAction valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }
    
}
