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
    
    public static EzyAccessAppException maximumUser(String appName, EzyMaxUserException e) {
        String message = "app: " + appName + " has maximum user";
        return new EzyAccessAppException(message, EzyAccessAppError.MAXIMUM_USER, e);
    }
    
    public static EzyAccessAppException maximumUser(String appName, int current, int max) {
        return maximumUser(appName, new EzyMaxUserException(current, max));
    }
    
}
