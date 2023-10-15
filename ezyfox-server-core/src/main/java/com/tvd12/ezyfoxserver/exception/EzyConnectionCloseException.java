package com.tvd12.ezyfoxserver.exception;

import java.io.IOException;

public class EzyConnectionCloseException extends IOException {

    public EzyConnectionCloseException(String message) {
        super(message);
    }

    public EzyConnectionCloseException(String message, Throwable e) {
        super(message, e);
    }
}
