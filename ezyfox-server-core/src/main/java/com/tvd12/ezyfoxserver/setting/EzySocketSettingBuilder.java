package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfoxserver.constant.SslType;

public class EzySocketSettingBuilder extends
    EzyAbstractSocketSettingBuilder<
        EzySimpleSocketSetting,
        EzySocketSettingBuilder> {

    protected SslType sslType;
    protected int sslHandshakeTimeout;
    protected int maxRequestSize;
    protected boolean tcpNoDelay;
    protected int writerThreadPoolSize;

    public EzySocketSettingBuilder() {
        this.port = 3005;
        this.sslType = SslType.CUSTOMIZATION;
        this.sslHandshakeTimeout = 350;
        this.maxRequestSize = 32768;
        this.writerThreadPoolSize = 8;
        this.codecCreator = "com.tvd12.ezyfox.codec.MsgPackCodecCreator";
    }

    public EzySocketSettingBuilder sslType(SslType sslType) {
        this.sslType = sslType;
        return this;
    }

    public EzySocketSettingBuilder sslHandshakeTimeout(int sslHandshakeTimeout) {
        this.sslHandshakeTimeout = sslHandshakeTimeout;
        return this;
    }

    public EzySocketSettingBuilder maxRequestSize(int maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
        return this;
    }

    public EzySocketSettingBuilder tcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    public EzySocketSettingBuilder writerThreadPoolSize(int writerThreadPoolSize) {
        this.writerThreadPoolSize = writerThreadPoolSize;
        return this;
    }

    @Override
    protected EzySimpleSocketSetting newSetting() {
        EzySimpleSocketSetting setting = new EzySimpleSocketSetting();
        setting.setTcpNoDelay(tcpNoDelay);
        setting.setSslActive(sslActive);
        setting.setSslType(sslType);
        setting.setSslHandshakeTimeout(sslHandshakeTimeout);
        setting.setMaxRequestSize(maxRequestSize);
        setting.setWriterThreadPoolSize(writerThreadPoolSize);
        setting.setCodecCreator(codecCreator);
        return setting;
    }
}
