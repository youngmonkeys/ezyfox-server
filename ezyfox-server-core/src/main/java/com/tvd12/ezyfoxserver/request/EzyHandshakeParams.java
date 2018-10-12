package com.tvd12.ezyfoxserver.request;

public interface EzyHandshakeParams extends EzyRequestParams {

    String getClientId();
    
    String getClientKey();
    
    String getClientType();
    
    String getClientVersion();
    
    String getToken();
    
    boolean isEnableEncryption();
    
}
