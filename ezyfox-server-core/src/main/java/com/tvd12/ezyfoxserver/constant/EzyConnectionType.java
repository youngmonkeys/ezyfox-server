package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;

import lombok.Getter;

public enum EzyConnectionType implements EzyConstant {

    SOCKET(1),
    WEBSOCKET(2);
    
    @Getter
    private final int id;
    
    private EzyConnectionType(int id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
}
