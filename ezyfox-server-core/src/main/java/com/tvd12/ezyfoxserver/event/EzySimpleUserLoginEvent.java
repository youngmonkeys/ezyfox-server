package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySession;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EzySimpleUserLoginEvent
    extends EzySimpleSessionEvent
    implements EzyUserLoginEvent {

    protected final EzyData data;
    protected final String zoneName;
    @Setter
    protected String username;
    @Setter
    protected String password;
    @Setter
    protected EzyData output;
    @Setter
    protected boolean streamingEnable;

    protected Map<Object, Object> userProperties;

    public EzySimpleUserLoginEvent(
        EzySession session,
        String zoneName,
        String username, String password, EzyData data) {
        super(session);
        this.zoneName = zoneName;
        this.data = data;
        this.username = username;
        this.password = password;
        this.streamingEnable = true;
        this.userProperties = new HashMap<>();
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    @Override
    public void setUserProperty(Object key, Object value) {
        this.userProperties.put(key, value);
    }

    @Override
    public void setUserProperties(Map<Object, Object> properties) {
        this.userProperties.putAll(properties);
    }

    @Override
    public void release() {
        super.release();
        this.username = null;
        this.password = null;
        this.output = null;
        this.userProperties.clear();
        this.userProperties = null;
    }
}
