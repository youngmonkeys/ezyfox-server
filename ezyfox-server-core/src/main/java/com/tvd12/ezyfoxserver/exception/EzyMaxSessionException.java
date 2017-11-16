package com.tvd12.ezyfoxserver.exception;

public class EzyMaxSessionException extends IllegalStateException {
    private static final long serialVersionUID = 7633864801826456518L;

    public EzyMaxSessionException(int current, int max) {
        super(getMessage(current, max));
    }
    
    private static String getMessage(int current, int max) {
        return new StringBuilder()
                .append("max sessions (max = ")
                .append(max)
                .append(", current = ")
                .append(current)
                .append(")")
                .toString();
    }
       
}
