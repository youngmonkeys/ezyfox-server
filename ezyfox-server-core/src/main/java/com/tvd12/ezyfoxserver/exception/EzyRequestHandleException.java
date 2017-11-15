package com.tvd12.ezyfoxserver.exception;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

@Getter
public class EzyRequestHandleException extends IllegalStateException {
    private static final long serialVersionUID = 6288790755559864267L;

    private Object data;
    private EzyConstant command;

    public EzyRequestHandleException(EzyConstant cmd, Object data, Throwable e) {
        super(newHandleRequestErrorMessage(cmd, data), e);
        this.data = data;
        this.command = cmd;
    }
    
    public static EzyRequestHandleException requestHandleException(
            EzyConstant cmd, Object data, Throwable e) {
        return new EzyRequestHandleException(cmd, data, e);
    }

    protected static String newHandleRequestErrorMessage(EzyConstant cmd, Object data) {
        return "error when handle request command: " + cmd + ", data: " + data;
    }

}
