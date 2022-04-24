package com.tvd12.ezyfoxserver.exception;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;

@Getter
public class EzyRequestHandleException extends IllegalStateException {
    private static final long serialVersionUID = 6288790755559864267L;

    protected final EzySession session;
    protected final EzyConstant command;
    protected final Object data;

    public EzyRequestHandleException(
        EzySession session,
        EzyConstant cmd, Object data, Throwable e) {
        super(newHandleRequestErrorMessage(session, cmd, data), e);
        this.data = data;
        this.command = cmd;
        this.session = session;
    }

    public static EzyRequestHandleException requestHandleException(
        EzySession session,
        EzyConstant cmd,
        Object data, Throwable e) {
        return new EzyRequestHandleException(session, cmd, data, e);
    }

    protected static String newHandleRequestErrorMessage(
        EzySession session,
        EzyConstant cmd, Object data) {
        return new StringBuilder()
            .append("error when handle request from: ")
            .append(session.getName())
            .append(", command: ")
            .append(cmd)
            .append(", data: ")
            .append(data)
            .toString();
    }

}
