package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyThreadPoolSizeSettingBuilder 
        implements EzyBuilder<EzySimpleThreadPoolSizeSetting> {
    
    protected int statistics                 = 1;
    protected int streamHandler              = 8;
    protected int socketDataReceiver         = 8;
    protected int systemRequestHandler       = 8;
    protected int extensionRequestHandler    = 8;
    protected int socketDisconnectionHandler = 2;
    protected int socketUserRemovalHandler   = 3;

    public EzyThreadPoolSizeSettingBuilder statistics(int statistics) {
        this.statistics = statistics;
        return this;
    }

    public EzyThreadPoolSizeSettingBuilder streamHandler(int streamHandler) {
        this.streamHandler = streamHandler;
        return this;
    }
    
    public EzyThreadPoolSizeSettingBuilder socketDataReceiver(int socketDataReceiver) {
        this.socketDataReceiver = socketDataReceiver;
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
        p.setStatistics(statistics);
        p.setStreamHandler(streamHandler);
        p.setSocketDataReceiver(socketDataReceiver);
        p.setSystemRequestHandler(systemRequestHandler);
        p.setExtensionRequestHandler(extensionRequestHandler);
        p.setSocketDisconnectionHandler(socketDisconnectionHandler);
        p.setSocketUserRemovalHandler(socketUserRemovalHandler);
        return p;
    }

}
