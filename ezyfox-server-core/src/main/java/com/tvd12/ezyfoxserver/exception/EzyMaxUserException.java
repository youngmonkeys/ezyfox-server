package com.tvd12.ezyfoxserver.exception;

public class EzyMaxUserException extends IllegalStateException {
    private static final long serialVersionUID = 7633864801826456518L;

    public EzyMaxUserException(int current, int max) {
        super(getMessage(current, max));
    }

    private static String getMessage(int current, int max) {
        return "max user (max = " +
            max +
            ", current = " +
            current +
            ")";
    }
}
