package com.tvd12.ezyfoxserver.wrapper.impl;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.controller.*;
import com.tvd12.ezyfoxserver.interceptor.EzyInterceptor;
import com.tvd12.ezyfoxserver.interceptor.EzyRawBytesInterceptor;
import com.tvd12.ezyfoxserver.interceptor.EzyServerUserInterceptor;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("rawtypes")
public class EzyServerControllersImpl implements EzyServerControllers {

    @Getter
    protected final EzyInterceptor streamingInterceptor;
    @Getter
    protected final EzyStreamingController streamingController;
    protected final Map<EzyConstant, EzyController> controllers;
    protected final Map<EzyConstant, EzyInterceptor> interceptors;

    protected EzyServerControllersImpl(Builder builder) {
        this.controllers = builder.newControllers();
        this.interceptors = builder.newInterceptors();
        this.streamingInterceptor = new EzyRawBytesInterceptor();
        this.streamingController = new EzySimpleStreamingController();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public EzyController getController(EzyConstant cmd) {
        EzyController controller = controllers.get(cmd);
        return controller;
    }

    @Override
    public EzyInterceptor getInterceptor(EzyConstant cmd) {
        EzyInterceptor interceptor = interceptors.get(cmd);
        return interceptor;
    }

    public static class Builder {

        protected Map<EzyConstant, EzyController> newControllers() {
            Map<EzyConstant, EzyController> answer = new ConcurrentHashMap<>();
            answer.put(EzyCommand.PING, new EzyPingController());
            answer.put(EzyCommand.HANDSHAKE, new EzyHandshakeController());
            answer.put(EzyCommand.LOGIN, new EzyLoginController());
            answer.put(EzyCommand.APP_ACCESS, new EzyAccessAppController());
            answer.put(EzyCommand.APP_REQUEST, new EzyRequestAppController());
            answer.put(EzyCommand.APP_EXIT, new EzyExitAppController());
            answer.put(EzyCommand.PLUGIN_INFO, new EzyPluginInfoController());
            answer.put(EzyCommand.PLUGIN_REQUEST, new EzyRequestPluginController());
            return answer;
        }

        protected Map<EzyConstant, EzyInterceptor> newInterceptors() {
            Map<EzyConstant, EzyInterceptor> answer = new ConcurrentHashMap<>();
            answer.put(EzyCommand.PING, EzyInterceptor.ALWAYS_PASS);
            answer.put(EzyCommand.HANDSHAKE, EzyInterceptor.ALWAYS_PASS);
            answer.put(EzyCommand.LOGIN, EzyInterceptor.ALWAYS_PASS);
            answer.put(EzyCommand.APP_ACCESS, new EzyServerUserInterceptor());
            answer.put(EzyCommand.APP_REQUEST, new EzyServerUserInterceptor());
            answer.put(EzyCommand.APP_EXIT, new EzyServerUserInterceptor());
            answer.put(EzyCommand.PLUGIN_INFO, new EzyServerUserInterceptor());
            answer.put(EzyCommand.PLUGIN_REQUEST, new EzyServerUserInterceptor());
            return answer;
        }

        public EzyServerControllers build() {
            return new EzyServerControllersImpl(this);
        }
    }
}
