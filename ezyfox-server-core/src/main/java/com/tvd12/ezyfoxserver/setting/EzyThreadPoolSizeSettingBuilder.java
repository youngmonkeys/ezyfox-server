package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyThreadPoolSizeSettingBuilder 
        implements EzyBuilder<EzySimpleThreadPoolSizeSetting> {
    
    protected int codec                      = 3;
    protected int statistics                 = 1;
    protected int streamHandler              = 8;
    protected int systemRequestHandler       = 8;
    protected int extensionRequestHandler    = 8;
    protected int socketDisconnectionHandler = 2;
    protected int socketUserRemovalHandler   = 3;

    public EzyThreadPoolSizeSettingBuilder codec(int codec) {
        this.codec = codec;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder statistics(int statistics) {
        this.statistics = statistics;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder streamHandler(int streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder systemRequestHandler(int systemRequestHandler) {
        this.systemRequestHandler = systemRequestHandler;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder extensionRequestHandler(int extensionRequestHandler) {
        this.extensionRequestHandler = extensionRequestHandler;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder socketDisconnectionHandler(int socketDisconnectionHandler) {
        this.socketDisconnectionHandler = socketDisconnectionHandler;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder socketUserRemovalHandler(int socketUserRemovalHandler) {
        this.socketUserRemovalHandler = socketUserRemovalHandler;
        return this;
    }

    @Override
    public EzySimpleThreadPoolSizeSetting build() {
        EzySimpleThreadPoolSizeSetting p = new EzySimpleThreadPoolSizeSetting();
        p.setCodec(codec);
        p.setStatistics(statistics);
        p.setStreamHandler(streamHandler);
        p.setSystemRequestHandler(systemRequestHandler);
        p.setExtensionRequestHandler(extensionRequestHandler);
        p.setSocketDisconnectionHandler(socketDisconnectionHandler);
        p.setSocketUserRemovalHandler(socketUserRemovalHandler);
        return p;
    }

}
