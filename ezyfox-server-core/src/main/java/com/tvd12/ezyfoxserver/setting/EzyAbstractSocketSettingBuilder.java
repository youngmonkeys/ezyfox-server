package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("unchecked")
public abstract class EzyAbstractSocketSettingBuilder<
    T extends EzyAbstractSocketSetting,
    B extends EzyAbstractSocketSettingBuilder<T, B>>
    implements EzyBuilder<EzyAbstractSocketSetting> {

    protected int port;
    protected String address = "0.0.0.0";
    protected boolean active = true;
    protected String codecCreator = null;
    protected boolean sslActive = false;

    public B port(int port) {
        this.port = port;
        return (B) this;
    }

    public B address(String address) {
        this.address = address;
        return (B) this;
    }

    public B active(boolean active) {
        this.active = active;
        return (B) this;
    }

    public B sslActive(boolean sslActive) {
        this.sslActive = sslActive;
        return (B) this;
    }

    public B codecCreator(String codecCreator) {
        this.codecCreator = codecCreator;
        return (B) this;
    }

    public B codecCreator(Class<?> codecCreator) {
        return codecCreator(codecCreator.getName());
    }

    @Override
    public T build() {
        T p = newSetting();
        p.setPort(port);
        p.setAddress(address);
        p.setActive(active);
        p.setCodecCreator(codecCreator);
        return p;
    }

    protected abstract T newSetting();
}
