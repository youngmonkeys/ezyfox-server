package com.tvd12.ezyfoxserver.support.asm;

import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.factory.EzyResponseFactory;
import lombok.Getter;
import lombok.Setter;

public abstract class EzyAsmAbstractRequestHandler implements EzyAsmRequestHandler {

    @Getter
    @Setter
    protected String command;

    @Setter
    protected EzyResponseFactory responseFactory;

    @Override
    public void handle(
        EzyContext context, EzyUserSessionEvent event, Object data) {
        try {
            handleRequest(context, event, data);
        } catch (Exception e) {
            handleException(context, event, data, e);
        }
    }

    public abstract void handleRequest(
        EzyContext context, EzyUserSessionEvent event, Object data);

    public abstract void handleException(
        EzyContext context,
        EzyUserSessionEvent event, Object data, Exception exception);

    protected void responseToSession(EzyUserSessionEvent event, Object data) {
        responseFactory.newObjectResponse()
            .command(command)
            .data(data)
            .session(event.getSession())
            .execute();
    }
}
