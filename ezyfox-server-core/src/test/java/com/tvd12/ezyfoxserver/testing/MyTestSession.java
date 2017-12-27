package com.tvd12.ezyfoxserver.testing;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class MyTestSession extends EzyAbstractSession {
    private static final long serialVersionUID = -1980181361152627223L;

    @Override
    public SocketAddress getClientAddress() {
        return new InetSocketAddress("123.123.123.123", 8080);
    }

    @Override
    public SocketAddress getServerAddress() {
        return new InetSocketAddress("localhost", 8080);
    }

    @Override
    public void close() {
    }

    @Override
    public void disconnect() {
    }

    @Override
    protected void sendData(EzyData data, EzyTransportType type) {
    }

    @Override
    public EzySessionDelegate getDelegate() {
        return new EzySessionDelegate() {
            
            @Override
            public void onSessionLoggedIn(EzyUser user) {
                
            }
            
            @Override
            public void onSessionRemoved(EzyConstant reason) {
                
            }
        };
    }
}
