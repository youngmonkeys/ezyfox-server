package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.util.EzyEnums;

import lombok.Getter;

public enum EzyLoginError implements EzyILoginError {

    INVALID_DATA(0, "invalid data"),
    ALREADY_LOGIN(1, "already logged in"),
    INVALID_USERNAME(2, "invalid user name"),
    INVALID_PASSWORD(3, "invalid password"),
    INVALID_TOKEN(4, "invalid token"),
    MAXIMUM_SESSION(5, "has gotten maximum sessions"),
    MAXIMUM_USER(6, "server has maximum users"),
    ZONE_NOT_FOUND(7, "zone not found"),
    SERVER_ERROR(8, "server error");
    
    @Getter
    private final int id;
    
    @Getter
    private final String message;
    
    private EzyLoginError(int id, String message) {
        this.id = id;
        this.message = message;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
    public static EzyLoginError valueOf(int id) {
        return EzyEnums.valueOf(values(), id);
    }
    
}
