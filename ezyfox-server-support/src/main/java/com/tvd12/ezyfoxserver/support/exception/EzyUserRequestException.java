package com.tvd12.ezyfoxserver.support.exception;

import lombok.Getter;

@Getter
public class EzyUserRequestException extends IllegalStateException {
    private static final long serialVersionUID = 2586181034307827101L;

    protected final String command;
    protected final Object data;

    public EzyUserRequestException(
            String command,
            Object data,
            Exception e) {
        super("handle request: " + command + " with data: " +  data + " error", e);
        this.command = command;
        this.data = data;
    }
}
