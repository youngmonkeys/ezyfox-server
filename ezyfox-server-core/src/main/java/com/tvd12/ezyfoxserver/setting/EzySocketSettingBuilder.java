package com.tvd12.ezyfoxserver.setting;

public class EzySocketSettingBuilder extends EzyAbstractSocketSettingBuilder<
        EzySimpleSocketSetting, EzySocketSettingBuilder> {

    protected int maxRequestSize;
    protected boolean tcpNoDelay;
    protected int writerThreadPoolSize;
    
    public EzySocketSettingBuilder() {
        this.port = 3005;
        this.maxRequestSize = 32768;
        this.writerThreadPoolSize = 8;
        this.codecCreator = "com.tvd12.ezyfox.codec.MsgPackCodecCreator";
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
        setting.setMaxRequestSize(maxRequestSize);
        setting.setWriterThreadPoolSize(writerThreadPoolSize);
        setting.setCodecCreator(codecCreator);
        return setting;
    }
    
}
