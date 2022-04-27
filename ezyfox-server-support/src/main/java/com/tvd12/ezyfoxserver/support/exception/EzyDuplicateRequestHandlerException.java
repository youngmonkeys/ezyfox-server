package com.tvd12.ezyfoxserver.support.exception;

import com.tvd12.ezyfoxserver.support.handler.EzyUserRequestHandler;

public class EzyDuplicateRequestHandlerException extends IllegalStateException {
    private static final long serialVersionUID = 2586181034307827101L;

    @SuppressWarnings("rawtypes")
    public EzyDuplicateRequestHandlerException(
        String command,
        EzyUserRequestHandler old, EzyUserRequestHandler now) {
        super("duplicate handler for: " + command + " <> " + old + " => " + now);
    }
}
