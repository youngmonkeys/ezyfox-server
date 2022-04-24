package com.tvd12.ezyfoxserver.exception;

public class EzyNotAuthorizedException extends Exception {
    private static final long serialVersionUID = 8292545377354181644L;

    public EzyNotAuthorizedException(String msg) {
        super(msg);
    }
}
