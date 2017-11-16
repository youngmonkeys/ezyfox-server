package com.tvd12.ezyfoxserver.exception;

import com.tvd12.ezyfoxserver.constant.EzyAccessAppError;
import com.tvd12.ezyfoxserver.constant.EzyIAccessAppError;

import lombok.Getter;

public class EzyAccessAppException extends IllegalStateException {
    private static final long serialVersionUID = 1432595688787320396L;

    @Getter
    private final EzyIAccessAppError error;
    
    public EzyAccessAppException(String message, EzyIAccessAppError error) {
        super(error.getMessage());
        this.error = error;
    }
    
    public EzyAccessAppException(String message, EzyIAccessAppError error, Exception e) {
        super(error.getMessage(), e);
        this.error = error;
    }
    
    public static EzyAccessAppException hasJoinedApp(String username, String appName) {
        String message = "user: " + username + " has joined app: " + appName;
        return new EzyAccessAppException(message, EzyAccessAppError.HAS_JOINED);
    }
    
    public static EzyAccessAppException maximumUser(String appName, EzyMaxUserException e) {
        String message = "app: " + appName + " has maximum user";
        return new EzyAccessAppException(message, EzyAccessAppError.MAXIMUM_USER, e);
    }
    
}
