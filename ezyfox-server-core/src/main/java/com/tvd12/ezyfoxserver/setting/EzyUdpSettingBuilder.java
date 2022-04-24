package com.tvd12.ezyfoxserver.setting;

public class EzyUdpSettingBuilder extends EzyAbstractSocketSettingBuilder<
    EzySimpleUdpSetting, EzyUdpSettingBuilder> {

    protected int maxRequestSize;
    protected int channelPoolSize;
    protected int handlerThreadPoolSize;

    public EzyUdpSettingBuilder() {
        this.port = 2611;
        this.active = false;
        this.maxRequestSize = 1024;
        this.channelPoolSize = 16;
        this.handlerThreadPoolSize = 5;
        this.codecCreator = "com.tvd12.ezyfox.codec.MsgPackCodecCreator";
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
    public EzySimpleUdpSetting newSetting() {
        EzySimpleUdpSetting p = new EzySimpleUdpSetting();
        p.setMaxRequestSize(maxRequestSize);
        p.setChannelPoolSize(channelPoolSize);
        p.setHandlerThreadPoolSize(handlerThreadPoolSize);
        return p;
    }
}
