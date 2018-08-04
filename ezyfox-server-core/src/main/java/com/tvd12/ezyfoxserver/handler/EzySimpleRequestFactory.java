package com.tvd12.ezyfoxserver.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.request.EzyRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleAccessAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleHandshakeRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleLoginRequest;
import com.tvd12.ezyfoxserver.request.EzySimplePingRequest;
import com.tvd12.ezyfoxserver.request.EzySimplePluginInfoRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestAppRequest;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestPluginRequestById;
import com.tvd12.ezyfoxserver.request.EzySimpleRequestPluginRequestByName;

@SuppressWarnings("rawtypes")
public class EzySimpleRequestFactory implements EzyRequestFactory {
    
    protected Map<EzyConstant, Supplier<EzyRequest>> suppliers;
    
    public EzySimpleRequestFactory() {
        this.suppliers = newSuppliers();
    }
    
    public EzyRequest newRequest(EzyConstant cmd) {
        Supplier<EzyRequest> supplier = suppliers.get(cmd);
        return supplier.get();
    }
    
    protected Map<EzyConstant, Supplier<EzyRequest>> newSuppliers() {
        Map<EzyConstant, Supplier<EzyRequest>> answer = new ConcurrentHashMap<>();
        answer.put(EzyCommand.PING, EzySimplePingRequest::new);
        answer.put(EzyCommand.HANDSHAKE, EzySimpleHandshakeRequest::new);
        answer.put(EzyCommand.LOGIN, EzySimpleLoginRequest::new);
        answer.put(EzyCommand.APP_ACCESS, EzySimpleAccessAppRequest::new);
        answer.put(EzyCommand.APP_REQUEST, EzySimpleRequestAppRequest::new);
        answer.put(EzyCommand.PLUGIN_INFO, EzySimplePluginInfoRequest::new);
        answer.put(EzyCommand.PLUGIN_REQUEST_BY_ID, EzySimpleRequestPluginRequestById::new);
        answer.put(EzyCommand.PLUGIN_REQUEST_BY_NAME, EzySimpleRequestPluginRequestByName::new);
        return answer;
    }
}