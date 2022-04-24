package com.tvd12.ezyfoxserver.event;

public interface EzyHandshakeEvent extends EzySessionEvent {

    String getClientId();

    byte[] getClientKey();

    String getClientType();

    String getClientVersion();

    String getReconnectToken();

    boolean isEnableEncryption();

    byte[] getSessionKey();

    void setSessionKey(byte[] sessionKey);

    byte[] getEncryptedSessionKey();

    void setEncryptedSessionKey(byte[] sessionKey);
}
