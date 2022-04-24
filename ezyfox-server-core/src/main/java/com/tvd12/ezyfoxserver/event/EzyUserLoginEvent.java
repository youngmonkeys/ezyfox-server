package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyData;

import java.util.Map;

public interface EzyUserLoginEvent extends EzySessionEvent {

    String getZoneName();

    EzyData getOutput();

    void setOutput(EzyData output);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    <T extends EzyData> T getData();

    Map<Object, Object> getUserProperties();

    void setUserProperties(Map<Object, Object> properties);

    boolean isStreamingEnable();

    void setStreamingEnable(boolean enable);

    void setUserProperty(Object key, Object value);

}
