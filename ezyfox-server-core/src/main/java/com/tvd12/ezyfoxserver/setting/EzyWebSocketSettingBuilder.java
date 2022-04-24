package com.tvd12.ezyfoxserver.setting;

public class EzyWebSocketSettingBuilder extends EzyAbstractSocketSettingBuilder<
    EzySimpleWebSocketSetting, EzyWebSocketSettingBuilder> {

    protected int sslPort;
    protected int maxFrameSize;
    protected int writerThreadPoolSize;
    protected EzySimpleSslConfigSetting sslConfig;
    protected boolean managementEnable;

    public EzyWebSocketSettingBuilder() {
        this.port = 2208;
        this.sslPort = 2812;
        this.maxFrameSize = 2048;
        this.writerThreadPoolSize = 8;
        this.managementEnable = false;
        this.sslConfig = new EzySimpleSslConfigSetting();
        this.codecCreator = "com.tvd12.ezyfox.codec.JacksonCodecCreator";
    }

    public EzyWebSocketSettingBuilder sslPort(int sslPort) {
        this.sslPort = sslPort;
        return this;
    }

    public EzyWebSocketSettingBuilder maxFrameSize(int maxFrameSize) {
        this.maxFrameSize = maxFrameSize;
        return this;
    }

    public EzyWebSocketSettingBuilder writerThreadPoolSize(int writerThreadPoolSize) {
        this.writerThreadPoolSize = writerThreadPoolSize;
        return this;
    }

    public EzyWebSocketSettingBuilder sslConfig(EzySimpleSslConfigSetting sslConfig) {
        this.sslConfig = sslConfig;
        return this;
    }

    public EzyWebSocketSettingBuilder managementEnable(boolean managementEnable) {
        this.managementEnable = managementEnable;
        return this;
    }

    @Override
    public EzySimpleWebSocketSetting newSetting() {
        EzySimpleWebSocketSetting p = new EzySimpleWebSocketSetting();
        p.setSslPort(sslPort);
        p.setMaxFrameSize(maxFrameSize);
        p.setWriterThreadPoolSize(writerThreadPoolSize);
        p.setSslActive(sslActive);
        p.setSslConfig(sslConfig);
        p.setManagementEnable(managementEnable);
        return p;
    }
}
