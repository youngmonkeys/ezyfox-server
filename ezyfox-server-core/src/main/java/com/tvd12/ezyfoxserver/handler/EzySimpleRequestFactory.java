package com.tvd12.ezyfoxserver.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleExitAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleLoginRequest;
import com.tvd12.ezyfoxserver.request.EzySimplePingRequest;
import com.tvd12.ezyfoxserver.request.EzySimplePluginInfoRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestPluginRequest;

@SuppressWarnings("rawtypes")
public class EzySimpleRequestFactory implements EzyRequestFactory {
    
    protected Map<EzyConstant, Supplier<EzySimpleRequest>> suppliers;
    
    public EzySimpleRequestFactory() {
        this.suppliers = newSuppliers();
    }
    
    public EzySimpleRequest newRequest(EzyConstant cmd) {
        Supplier<EzySimpleRequest> supplier = suppliers.get(cmd);
        return supplier.get();
    }
    
    protected Map<EzyConstant, Supplier<EzySimpleRequest>> newSuppliers() {
        Map<EzyConstant, Supplier<EzySimpleRequest>> answer = new ConcurrentHashMap<>();
        answer.put(EzyCommand.PING, () -> new EzySimplePingRequest());
        answer.put(EzyCommand.HANDSHAKE, () -> new EzySimpleHandshakeRequest());
        answer.put(EzyCommand.LOGIN, () -> new EzySimpleLoginRequest());
        answer.put(EzyCommand.APP_ACCESS, () -> new EzySimpleAccessAppRequest());
        answer.put(EzyCommand.APP_REQUEST, () -> new EzySimpleRequestAppRequest());
        answer.put(EzyCommand.APP_EXIT, () -> new EzySimpleExitAppRequest());
        answer.put(EzyCommand.PLUGIN_INFO, () -> new EzySimplePluginInfoRequest());
        answer.put(EzyCommand.PLUGIN_REQUEST, () -> new EzySimpleRequestPluginRequest());
        return answer;
    }
}