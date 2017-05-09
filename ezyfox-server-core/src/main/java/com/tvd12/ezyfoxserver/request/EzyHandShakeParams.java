package com.tvd12.ezyfoxserver.request;

public interface EzyHandShakeParams {

    String getClientId();
    
    String getClientKey();
    
    String getClientType();
    
    String getClientVersion();
    
    String getReconnectToken();
}
