package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyUdpSettingBuilder implements EzyBuilder<EzySimpleUdpSetting> {
    protected int port;
    protected String address;
    protected boolean active;
    protected int maxRequestSize;
    protected int channelPoolSize;
    protected int handlerThreadPoolSize;

    public EzyUdpSettingBuilder() {
        this.port = 2611;
        this.address = "0.0.0.0";
        this.active = false;
        this.maxRequestSize = 1024;
        this.channelPoolSize = 16;
        this.handlerThreadPoolSize = 5;
    }

    public EzyUdpSettingBuilder port(int port) {
        this.port = port;
        return this;
    }

    public EzyUdpSettingBuilder address(String address) {
        this.address = address;
        return this;
    }

    public EzyUdpSettingBuilder active(boolean active) {
        this.active = active;
        return this;
    }

    public EzyUdpSettingBuilder maxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
        return this;
    }

    public EzyUdpSettingBuilder channelPoolSize(int channelPoolSize) {
        this.channelPoolSize = channelPoolSize;
        return this;
    }

    public EzyUdpSettingBuilder handlerThreadPoolSize(int handlerThreadPoolSize) {
        this.handlerThreadPoolSize = handlerThreadPoolSize;
        return this;
    }

    @Override
    public EzySimpleUdpSetting build() {
        EzySimpleUdpSetting p = new EzySimpleUdpSetting();
        p.setPort(port);
        p.setAddress(address);
        p.setActive(active);
        p.setMaxRequestSize(maxRequestSize);
        p.setChannelPoolSize(channelPoolSize);
        p.setHandlerThreadPoolSize(handlerThreadPoolSize);
        return p;
    }
}
