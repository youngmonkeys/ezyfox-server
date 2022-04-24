package com.tvd12.ezyfoxserver.request;

public interface EzyHandshakeParams extends EzyRequestParams {

    String getClientId();

    byte[] getClientKey();

    String getClientType();

    String getClientVersion();

    String getReconnectToken();

    boolean isEnableEncryption();
}
